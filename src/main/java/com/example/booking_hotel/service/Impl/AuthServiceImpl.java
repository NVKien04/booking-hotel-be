package com.example.booking_hotel.service.Impl;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import com.example.booking_hotel.dto.request.auth.*;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.TokenResponse;
import com.example.booking_hotel.entity.RedisRevokedToken;
import com.example.booking_hotel.entity.RefreshToken;
import com.example.booking_hotel.enums.TokenType;
import com.example.booking_hotel.repository.RefreshTokenRepository;
import com.example.booking_hotel.repository.RevokedTokenCodeRepository;
import com.example.booking_hotel.service.EmailService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.booking_hotel.configuration.SecurityUtil;
import com.example.booking_hotel.dto.response.auth.AuthResponse;
import com.example.booking_hotel.dto.response.auth.IntrospectResponse;
import com.example.booking_hotel.entity.User;
import com.example.booking_hotel.enums.AccountStatus;
import com.example.booking_hotel.enums.Role;
import com.example.booking_hotel.exception.AppException;
import com.example.booking_hotel.exception.ErrorCode;
import com.example.booking_hotel.mapper.UserMapper;
import com.example.booking_hotel.repository.UserRepository;
import com.example.booking_hotel.repository.httpClient.OutboundIdentityClient;
import com.example.booking_hotel.repository.httpClient.OutboundUserClient;
import com.example.booking_hotel.service.AuthService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    RevokedTokenCodeRepository revokedTokenCodeRepository;
    RefreshTokenRepository refreshTokenRepository;


    @NonFinal
    @Value("${jwt.refreshKey}")
    private String REFRESH_KEY;

    @NonFinal
    @Value("${jwt.refresh-token.expiry-in-days}")//20
    private long refreshTokenExpiration;


    @NonFinal
    @Value("${jwt.resetKey}")
    private String RESET_KEY;

    @NonFinal
    @Value("${jwt.reset.expiry-in-minutes}")//15
    private long resetTokenExpiration;

    @NonFinal
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.access-token.expiry-in-minutes}")//15
    private long accessTokenExpiration;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

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

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    UserRepository userRepository;
    UserMapper userMapper;
    SecurityUtil securityUtil;
    OutboundIdentityClient outboundIdentityClient;
    OutboundUserClient outboundUserClient;
    EmailService emailService;

    @Transactional
    @Override
    public TokenResponse registerRenter(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        User user = userMapper.mapToUser(registerRequest);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.RENTER);
        user.setStatus(AccountStatus.UNVERIFIED);
        var rs = userRepository.save(user);
        emailService.senEmailUserWithRegister(user);
        return generateTokenAndSave(rs);// vd hàm này thực hiện thành công
    }

    @Transactional
    public TokenResponse authenticated(LoginRequest loginRequest) {
        User user = userRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticate = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        if (!authenticate) {
            throw new AppException(ErrorCode.BAD_CREDENTIALS);
        }
        return  generateTokenAndSave(user);
    }

    @Transactional
    public IntrospectResponse introspectResponse(IntrospectRequest introspectRequest) {

        String token = introspectRequest.getToken().toString();

        boolean invalidated = true;


            try {
                verifyToken(token, TokenType.ACCESS_TOKEN);    } catch (JOSEException | AppException | ParseException e) {
            invalidated = false;
        }

        return IntrospectResponse.builder().valid(invalidated).build();
    }



    private  String getKey (TokenType tokenType){
        switch (tokenType){
            case ACCESS_TOKEN -> {return SIGNER_KEY;}
            case REFRESH_TOKEN -> {return REFRESH_KEY;}
            case RESET_PASSWORD_TOKEN -> {return RESET_KEY;}
            default -> throw new AppException(ErrorCode.BAD_CREDENTIALS);
        }
    }

    private long getDurationByToken(TokenType type) {
        switch (type) {
            case ACCESS_TOKEN -> {return Duration.ofMinutes(accessTokenExpiration).getSeconds();}
            case REFRESH_TOKEN -> {return Duration.ofDays(refreshTokenExpiration).getSeconds();}
            case RESET_PASSWORD_TOKEN -> {return Duration.ofMinutes(resetTokenExpiration).getSeconds();}
            default -> throw new AppException(ErrorCode.BAD_CREDENTIALS);
        }
    }

    public String generateToken(User user, TokenType tokenType) {

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        long durationInSeconds = getDurationByToken(tokenType);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("bookingClone")
                .issueTime(new Date())
                .expirationTime(
                        new Date(Instant.now().plus(durationInSeconds, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("id", user.getId())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().getDisplayName())
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(getKey(tokenType)));
            return jwsObject.serialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ApiResponse<Void> logout(LogoutTokenRequest logoutTokenRequest) throws JOSEException, ParseException {
        var signToken = verifyToken(logoutTokenRequest.getAccessToken(), TokenType.ACCESS_TOKEN);

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
    public SignedJWT verifyToken(String token, TokenType tokenType) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(getKey(tokenType));
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime =  signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(jwsVerifier);

        if (!verified || expiryTime.before(new Date())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if(tokenType.equals(TokenType.ACCESS_TOKEN) &&  revokedTokenCodeRepository.existsById(token)){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if(tokenType.equals(TokenType.REFRESH_TOKEN) &&  !refreshTokenRepository.existsByRefreshToken(token)){
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        }
        return SignedJWT.parse(token);
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) throws JOSEException, ParseException {

        var signJWT = verifyToken(refreshToken, TokenType.REFRESH_TOKEN);
        var jit = signJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();
        var userEmail = signJWT.getJWTClaimsSet().getSubject();
        User user =
                userRepository.findByEmail(userEmail).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        var token = generateToken(user,TokenType.ACCESS_TOKEN);

        return TokenResponse.builder().
                 accessToken(token)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .build();
    }

    @Override
    public TokenResponse outboundAuthenticate(String code) {
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

            return generateTokenAndSave(user);
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
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticate = passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword());
        if (!authenticate) {
            throw new AppException(ErrorCode.INVALID_OLD_PASSWORD);
        }
        if(!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public TokenResponse generateTokenAndSave(User user) {
        String accessToken = generateToken(user, TokenType.ACCESS_TOKEN);
        String refreshToken = generateToken(user, TokenType.REFRESH_TOKEN);//hàm này có lỗi thì có rollback không
        saveRefreshToken(refreshToken);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .build();
    }

    @Override
    public void saveRefreshToken(String token) {

        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(token)
                .expiryTime(LocalDateTime.now().plusDays(refreshTokenExpiration))
                .build();
        refreshTokenRepository.save(refreshToken);
    }


}
