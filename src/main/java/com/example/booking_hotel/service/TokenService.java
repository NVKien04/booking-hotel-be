package com.example.booking_hotel.service;

import com.example.booking_hotel.dto.response.TokenResponse;
import com.example.booking_hotel.entity.User;
import com.example.booking_hotel.enums.TokenType;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public interface TokenService {

    void saveRefreshToken(String token);

    String generateToken(User user, TokenType tokenType);

    TokenResponse refreshToken(String refreshToken) throws JOSEException, ParseException;

    SignedJWT verifyToken(String token, TokenType tokenType) throws JOSEException, ParseException;

    TokenResponse generateTokenAndSave(User user);
}
