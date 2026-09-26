package com.example.booking_hotel.service;

import java.text.ParseException;

import com.example.booking_hotel.dto.response.AuthResponse;
import com.example.booking_hotel.entity.User;
import com.nimbusds.jose.JOSEException;

public interface RefreshTokenService {
    void saveRefreshToken(String token);

    AuthResponse refreshToken(String refreshToken) throws JOSEException, ParseException;

    AuthResponse generateTokenAndSave(User user);

    void deleteRefreshToken(String token);
}
