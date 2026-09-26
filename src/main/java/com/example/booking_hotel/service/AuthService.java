package com.example.booking_hotel.service;

import java.text.ParseException;

import com.example.booking_hotel.dto.request.*;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.AuthResponse;
import com.nimbusds.jose.JOSEException;

public interface AuthService {
    AuthResponse registerRenter(RegisterRequest registerRequest);

    AuthResponse authenticated(LoginRequest loginRequest);

    ApiResponse<Void> logout(LogoutTokenRequest logoutTokenRequest) throws JOSEException, ParseException;

    AuthResponse outboundAuthenticate(String code);

    void ChangePassword(ChangePasswordRequest changePasswordRequest);
}

