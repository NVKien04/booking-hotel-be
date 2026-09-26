package com.example.booking_hotel.service.Impl;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.booking_hotel.dto.request.IntrospectRequest;
import com.example.booking_hotel.dto.response.IntrospectResponse;
import com.example.booking_hotel.entity.User;
import com.example.booking_hotel.enums.TokenType;
import com.example.booking_hotel.exception.AppException;
import com.example.booking_hotel.exception.ErrorCode;
import com.example.booking_hotel.repository.RefreshTokenRepository;
import com.example.booking_hotel.repository.RevokedTokenCodeRepository;
import com.example.booking_hotel.service.JwtService;
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
public class JwtServiceImpl implements JwtService {

    RevokedTokenCodeRepository revokedTokenCodeRepository;
    RefreshTokenRepository refreshTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.refreshKey}")
    String REFRESH_KEY;

    @NonFinal
    @Value("${jwt.resetKey}")
    String RESET_KEY;

    @NonFinal
    @Value("${jwt.access-token.expiry-in-minutes:15}")
    long accessTokenExpiration;

    @NonFinal
    @Value("${jwt.refresh-token.expiry-in-days:20}")
    long refreshTokenExpiration;

    @NonFinal
    @Value("${jwt.reset.expiry-in-minutes:15}")
    long resetTokenExpiration;

    @Override
    public String generateToken(User user, TokenType tokenType) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        long durationInSeconds = getDurationByToken(tokenType);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("bookingClone")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now()
                        .plus(durationInSeconds, ChronoUnit.SECONDS)
                        .toEpochMilli()))
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
    public SignedJWT verifyToken(String token, TokenType tokenType) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(getKey(tokenType));
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(jwsVerifier);

        if (!verified || expiryTime.before(new Date())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if (tokenType.equals(TokenType.ACCESS_TOKEN) && revokedTokenCodeRepository.existsById(token)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if (tokenType.equals(TokenType.REFRESH_TOKEN) && !refreshTokenRepository.existsByRefreshToken(token)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    @Override
    public IntrospectResponse introspectResponse(IntrospectRequest introspectRequest) {
        String token = introspectRequest.getToken();
        boolean valid = true;
        try {
            verifyToken(token, TokenType.ACCESS_TOKEN);
        } catch (JOSEException | AppException | ParseException e) {
            valid = false;
        }
        return IntrospectResponse.builder().valid(valid).build();
    }

    @Override
    public long getDurationByToken(TokenType type) {
        switch (type) {
            case ACCESS_TOKEN -> {
                return Duration.ofMinutes(accessTokenExpiration).getSeconds();
            }
            case REFRESH_TOKEN -> {
                return Duration.ofDays(refreshTokenExpiration).getSeconds();
            }
            case RESET_PASSWORD_TOKEN -> {
                return Duration.ofMinutes(resetTokenExpiration).getSeconds();
            }
            default -> throw new AppException(ErrorCode.BAD_CREDENTIALS);
        }
    }

    private String getKey(TokenType tokenType) {
        switch (tokenType) {
            case ACCESS_TOKEN -> {
                return SIGNER_KEY;
            }
            case REFRESH_TOKEN -> {
                return REFRESH_KEY;
            }
            case RESET_PASSWORD_TOKEN -> {
                return RESET_KEY;
            }
            default -> throw new AppException(ErrorCode.BAD_CREDENTIALS);
        }
    }
}
