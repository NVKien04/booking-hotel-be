package com.example.booking_hotel.service;

import java.text.ParseException;

import com.example.booking_hotel.dto.response.AuthResponse;
import com.example.booking_hotel.entity.User;
import com.example.booking_hotel.enums.TokenType;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

public interface TokenService {

    void saveRefreshToken(String token);

    String generateToken(User user, TokenType tokenType);

    AuthResponse refreshToken(String refreshToken) throws JOSEException, ParseException;

    SignedJWT verifyToken(String token, TokenType tokenType) throws JOSEException, ParseException;

    AuthResponse generateTokenAndSave(User user);
}
