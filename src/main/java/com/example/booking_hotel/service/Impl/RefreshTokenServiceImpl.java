package com.example.booking_hotel.service.Impl;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booking_hotel.dto.response.AuthResponse;
import com.example.booking_hotel.entity.RefreshToken;
import com.example.booking_hotel.entity.User;
import com.example.booking_hotel.enums.TokenType;
import com.example.booking_hotel.exception.AppException;
import com.example.booking_hotel.exception.ErrorCode;
import com.example.booking_hotel.repository.RefreshTokenRepository;
import com.example.booking_hotel.repository.UserRepository;
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
public class RefreshTokenServiceImpl implements RefreshTokenService {

    RefreshTokenRepository refreshTokenRepository;
    UserRepository userRepository;
    JwtService jwtService;

    @NonFinal
    @Value("${jwt.refresh-token.expiry-in-days:20}")
    long refreshTokenExpiration;

    @NonFinal
    @Value("${jwt.access-token.expiry-in-minutes:15}")
    long accessTokenExpiration;

    @Transactional
    @Override
    public void saveRefreshToken(String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(token)
                .expiryTime(LocalDateTime.now().plusDays(refreshTokenExpiration))
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    @Override
    public AuthResponse refreshToken(String refreshToken) throws JOSEException, ParseException {
        var signJWT = jwtService.verifyToken(refreshToken, TokenType.REFRESH_TOKEN);
        var userEmail = signJWT.getJWTClaimsSet().getSubject();
        User user = userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var token = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);

        return AuthResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(Duration.ofMinutes(accessTokenExpiration).getSeconds())
                .authenticated(true)
                .build();
    }

    @Transactional
    @Override
    public AuthResponse generateTokenAndSave(User user) {
        String accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
        String refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN);
        saveRefreshToken(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(Duration.ofMinutes(accessTokenExpiration).getSeconds())
                .authenticated(true)
                .build();
    }

    @Transactional
    @Override
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByRefreshToken(token);
    }
}
