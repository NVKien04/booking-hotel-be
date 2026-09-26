package com.example.booking_hotel.service;

import java.text.ParseException;

import com.example.booking_hotel.dto.request.IntrospectRequest;
import com.example.booking_hotel.dto.response.IntrospectResponse;
import com.example.booking_hotel.entity.User;
import com.example.booking_hotel.enums.TokenType;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

public interface JwtService {
    String generateToken(User user, TokenType tokenType);

    SignedJWT verifyToken(String token, TokenType tokenType) throws JOSEException, ParseException;

    IntrospectResponse introspectResponse(IntrospectRequest introspectRequest);

    long getDurationByToken(TokenType type);
}
