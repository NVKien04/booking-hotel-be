package com.example.booking_hotel.service;

import java.text.ParseException;

import com.example.booking_hotel.dto.request.*;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.AuthResponse;
import com.example.booking_hotel.dto.response.IntrospectResponse;
import com.example.booking_hotel.entity.User;
import com.example.booking_hotel.enums.TokenType;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

public interface AuthService {
    AuthResponse registerRenter(RegisterRequest registerRequest);

    AuthResponse authenticated(LoginRequest loginRequest);

    IntrospectResponse introspectResponse(IntrospectRequest introspectRequest) throws JOSEException, ParseException;

    ApiResponse<Void> logout(LogoutTokenRequest logoutTokenRequest) throws JOSEException, ParseException;

    AuthResponse outboundAuthenticate(String code);

    void ChangePassword(ChangePasswordRequest changePasswordRequest);

    void saveRefreshToken(String token);

    String generateToken(User user, TokenType tokenType);

    AuthResponse refreshToken(String refreshToken) throws JOSEException, ParseException;

    SignedJWT verifyToken(String token, TokenType tokenType) throws JOSEException, ParseException;

    AuthResponse generateTokenAndSave(User user);
}
