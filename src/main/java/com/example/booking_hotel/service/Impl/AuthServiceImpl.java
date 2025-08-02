package com.example.booking_hotel.service.Impl;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import com.example.booking_hotel.dto.request.auth.ExChangeTokenRequest;
import com.example.booking_hotel.repository.httpClient.OutboundIdentityClient;
import com.example.booking_hotel.repository.httpClient.OutboundUserClient;
import com.example.booking_hotel.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.booking_hotel.configuration.SecurityUtil;
import com.example.booking_hotel.dto.request.auth.IntrospectRequest;
import com.example.booking_hotel.dto.request.auth.LoginRequest;
import com.example.booking_hotel.dto.request.auth.RegisterRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.auth.AuthResponse;
import com.example.booking_hotel.dto.response.auth.IntrospectResponse;
import com.example.booking_hotel.entity.InvalidatedToken;
import com.example.booking_hotel.entity.User;
import com.example.booking_hotel.enums.Role;
import com.example.booking_hotel.exception.AppException;
import com.example.booking_hotel.exception.ErrorCode;
import com.example.booking_hotel.mapper.UserMapper;
import com.example.booking_hotel.repository.InvalidatedTokenRepository;
import com.example.booking_hotel.repository.UserRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

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
    InvalidatedTokenRepository invalidatedToken;
    SecurityUtil securityUtil;
    OutboundIdentityClient outboundIdentityClient;
    OutboundUserClient outboundUserClient;

    @Override
    public AuthResponse registerRenter(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getUsername())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        User user = userMapper.mapToUser(registerRequest);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.RENTER);
        var rs = userRepository.save(user);
        var token = generateToken(rs);
        return AuthResponse.builder().authenticated(true).token(token).build();
    }

    public AuthResponse authenticated(LoginRequest loginRequest) {
        User user = userRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticate = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        if (!authenticate) {
            throw new AppException(ErrorCode.BAD_CREDENTIALS);
        }
        var token = generateToken(user);
        return AuthResponse.builder().authenticated(true).token(token).build();
    }

    public IntrospectResponse introspectResponse(IntrospectRequest introspectRequest) {

        String token = introspectRequest.getToken().toString();

        boolean invalidated = true;

        try {
            verifyToken(token, false);
        } catch (JOSEException | AppException | ParseException e) {
            invalidated = false;
        }

        return IntrospectResponse.builder().valid(invalidated).build();
    }

    public String generateToken(User user) {

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("bookingClone")
                .issueTime(new Date())
                .expirationTime(
                        new Date(Instant.now().plus(3600, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("id", user.getId())
                .claim("name", user.getUsername())
                .claim("email", user.getEmail())
                .claim("avatar", user.getAvatar_img())
                .claim("role", user.getRole().getDisplayName())
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY));
            return jwsObject.serialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public ApiResponse<Void> logout(String token) throws JOSEException, ParseException {
        var signToken = verifyToken(token, false);

        String jid = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().jwtID(jid).expiryTime(expiryTime).build();
        invalidatedTokenRepository.save(invalidatedToken);
        return ApiResponse.<Void>builder().message("logged out successfully").build();
    }

    @Override
    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(jwsVerifier);

        if (!verified || expiryTime.before(new Date())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        return SignedJWT.parse(token);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) throws JOSEException, ParseException {

        var signJWT = verifyToken(refreshToken, true);
        var jit = signJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().jwtID(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);
        var userEmail = signJWT.getJWTClaimsSet().getSubject();
        User user =
                userRepository.findByEmail(userEmail).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        var token = generateToken(user);

        return AuthResponse.builder().token(token).authenticated(true).build();
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

        var userInfo = outboundUserClient.getUserInfo("json", response.getAccessToken());


        User user = userRepository.findByEmail(userInfo.getEmail()).orElseGet(

                () -> userRepository.save(User.builder()
                                .username(userInfo.getName())
                                .email(userInfo.getEmail())
                                .avatar_img(userInfo.getPicture())
                                .role(Role.RENTER)
                        .build())
        );

            log.info("Token response: {}", userInfo);
            log.info("Token response: {}", user);

            var token = generateToken(user);




            return AuthResponse.builder()
                    .token(token)
                    .build();
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
}
