package com.example.booking_hotel.service.Impl;

import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booking_hotel.configuration.SecurityUtil;
import com.example.booking_hotel.dto.request.ChangePasswordRequest;
import com.example.booking_hotel.dto.request.ExChangeTokenRequest;
import com.example.booking_hotel.dto.request.LoginRequest;
import com.example.booking_hotel.dto.request.LogoutTokenRequest;
import com.example.booking_hotel.dto.request.RegisterRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.AuthResponse;
import com.example.booking_hotel.entity.RedisRevokedToken;
import com.example.booking_hotel.entity.User;
import com.example.booking_hotel.enums.AccountStatus;
import com.example.booking_hotel.enums.Role;
import com.example.booking_hotel.enums.TokenType;
import com.example.booking_hotel.exception.AppException;
import com.example.booking_hotel.exception.ErrorCode;
import com.example.booking_hotel.mapper.UserMapper;
import com.example.booking_hotel.repository.RevokedTokenCodeRepository;
import com.example.booking_hotel.repository.UserRepository;
import com.example.booking_hotel.repository.httpClient.OutboundIdentityClient;
import com.example.booking_hotel.repository.httpClient.OutboundUserClient;
import com.example.booking_hotel.service.AuthService;
import com.example.booking_hotel.service.EmailService;
import com.example.booking_hotel.service.JwtService;
import com.example.booking_hotel.service.RefreshTokenService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    UserMapper userMapper;
    SecurityUtil securityUtil;
    PasswordEncoder passwordEncoder;
    OutboundIdentityClient outboundIdentityClient;
    OutboundUserClient outboundUserClient;
    EmailService emailService;
    JwtService jwtService;
    RefreshTokenService refreshTokenService;
    RevokedTokenCodeRepository revokedTokenCodeRepository;

    @NonFinal
    @Value("${outbound.identity.client-id}")
    protected String CLIENT_ID;

    @NonFinal
    @Value("${outbound.identity.client-secret}")
    protected String CLIENT_SECRET;

    @NonFinal
    @Value("${outbound.identity.redirect-uri}")
    protected String REDIRECT_URI;

    @NonFinal
    protected final String GRANT_TYPE = "authorization_code";

    @Transactional
    @Override
    public AuthResponse registerRenter(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        User user = userMapper.mapToUser(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.RENTER);
        user.setStatus(AccountStatus.UNVERIFIED);
        var rs = userRepository.save(user);
        emailService.senEmailUserWithRegister(user);
        return refreshTokenService.generateTokenAndSave(rs);
    }

    @Transactional
    @Override
    public AuthResponse authenticated(LoginRequest loginRequest) {
        User user = userRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticate = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        if (!authenticate) {
            throw new AppException(ErrorCode.BAD_CREDENTIALS);
        }
        return refreshTokenService.generateTokenAndSave(user);
    }

    @Override
    public ApiResponse<Void> logout(LogoutTokenRequest logoutTokenRequest) throws JOSEException, ParseException {
        var signToken = jwtService.verifyToken(logoutTokenRequest.getAccessToken(), TokenType.ACCESS_TOKEN);

        String email = signToken.getJWTClaimsSet().getSubject();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        RedisRevokedToken redisRevokedToken = RedisRevokedToken.builder()
                .accessToken(logoutTokenRequest.getAccessToken())
                .email(email)
                .expiryTime(expiryTime)
                .ttl((expiryTime.getTime() - System.currentTimeMillis()) / 1000)
                .build();
        revokedTokenCodeRepository.save(redisRevokedToken);
        return ApiResponse.<Void>builder().message("logged out successfully").build();
    }

    @Override
    public AuthResponse outboundAuthenticate(String code) {
        try {
            var response = outboundIdentityClient.exchangeToken(ExChangeTokenRequest.builder()
                    .code(code)
                    .clientId(CLIENT_ID)
                    .clientSecret(CLIENT_SECRET)
                    .redirectUri(REDIRECT_URI)
                    .grantType(GRANT_TYPE)
                    .build());

            log.warn(CLIENT_SECRET);
            log.warn(CLIENT_ID);
            log.warn(REDIRECT_URI);
            var userInfo = outboundUserClient.getUserInfo("json", response.getAccessToken());

            User user = userRepository
                    .findByEmail(userInfo.getEmail())
                    .orElseGet(() -> userRepository.save(User.builder()
                            .username(userInfo.getName())
                            .email(userInfo.getEmail())
                            .avatar_img(userInfo.getPicture())
                            .role(Role.RENTER)
                            .status(AccountStatus.VERIFIED)
                            .build()));

            log.info("Token response: {}", userInfo);
            log.info("Token response: {}", user);

            return refreshTokenService.generateTokenAndSave(user);
        } catch (feign.FeignException.BadRequest e) {
            log.error("Google từ chối mã code: {}", e.getMessage());
            throw new RuntimeException("Google xác thực thất bại: Mã code không hợp lệ.");
        } catch (feign.FeignException e) {
            log.error("Lỗi Feign khi gọi Google: {}", e.getMessage());
            throw new RuntimeException("Không thể kết nối tới máy chủ xác thực.");
        } catch (Exception e) {
            log.error("Lỗi không xác định: {}", e.getMessage());
            throw new RuntimeException("Xác thực thất bại.");
        }
    }

    @Override
    public void ChangePassword(ChangePasswordRequest changePasswordRequest) {
        String userId = securityUtil.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticate = passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword());
        if (!authenticate) {
            throw new AppException(ErrorCode.INVALID_OLD_PASSWORD);
        }
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
