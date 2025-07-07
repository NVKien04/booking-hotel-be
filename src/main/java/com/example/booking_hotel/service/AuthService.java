package com.example.booking_hotel.service;

import java.text.ParseException;

import com.example.booking_hotel.dto.request.auth.IntrospectRequest;
import com.example.booking_hotel.dto.request.auth.LoginRequest;
import com.example.booking_hotel.dto.request.auth.RegisterRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.auth.AuthResponse;
import com.example.booking_hotel.dto.response.auth.IntrospectResponse;
import com.example.booking_hotel.entity.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

public interface AuthService {
    AuthResponse registerRenter(RegisterRequest registerRequest);

    AuthResponse authenticated(LoginRequest loginRequest);

    IntrospectResponse introspectResponse(IntrospectRequest introspectRequest) throws JOSEException, ParseException;

    String generateToken(User user);

    ApiResponse<Void> logout(String token) throws JOSEException, ParseException;

    SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException;

    AuthResponse refreshToken(String refreshToken) throws JOSEException, ParseException;
}
