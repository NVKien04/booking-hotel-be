package com.example.booking_hotel.service;

import com.example.booking_hotel.dto.request.auth.IntrospectRequest;
import com.example.booking_hotel.dto.request.auth.LoginRequest;
import com.example.booking_hotel.dto.request.auth.RegisterRequest;
import com.example.booking_hotel.dto.response.auth.AuthResponse;
import com.example.booking_hotel.dto.response.auth.IntrospectResponse;
import com.example.booking_hotel.dto.response.user.UserResponse;
import com.example.booking_hotel.entity.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public interface AuthService {
     UserResponse registerRenter(RegisterRequest registerRequest);

     AuthResponse authenticated(LoginRequest loginRequest);

     IntrospectResponse introspectResponse(IntrospectRequest introspectRequest) throws JOSEException, ParseException;

     String generateToken(User user);

     SignedJWT verifyToken(String token) throws JOSEException, ParseException;

}
