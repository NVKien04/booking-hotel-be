package com.example.booking_hotel.service;

import java.text.ParseException;

import com.example.booking_hotel.dto.request.auth.*;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.TokenResponse;
import com.example.booking_hotel.dto.response.auth.AuthResponse;
import com.example.booking_hotel.dto.response.auth.IntrospectResponse;
import com.example.booking_hotel.entity.User;
import com.example.booking_hotel.enums.TokenType;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

public interface
AuthService {
    TokenResponse registerRenter(RegisterRequest registerRequest);

    TokenResponse authenticated(LoginRequest loginRequest);

    IntrospectResponse introspectResponse(IntrospectRequest introspectRequest) throws JOSEException, ParseException;

    ApiResponse<Void> logout(LogoutTokenRequest logoutTokenRequest) throws JOSEException, ParseException;

    TokenResponse outboundAuthenticate(String code);

    void ChangePassword(ChangePasswordRequest changePasswordRequest);

    void saveRefreshToken(String token);

    String generateToken(User user, TokenType tokenType);

    TokenResponse refreshToken(String refreshToken) throws JOSEException, ParseException;

    SignedJWT verifyToken(String token, TokenType tokenType) throws JOSEException, ParseException;

    TokenResponse generateTokenAndSave(User user);
}
