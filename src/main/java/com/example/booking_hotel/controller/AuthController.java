package com.example.booking_hotel.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.booking_hotel.dto.request.auth.IntrospectRequest;
import com.example.booking_hotel.dto.request.auth.LoginRequest;
import com.example.booking_hotel.dto.request.auth.RegisterRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.auth.AuthResponse;
import com.example.booking_hotel.service.AuthService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping("/register")
    ApiResponse<AuthResponse> registerRenter(@RequestBody RegisterRequest registerRequest) {
        var token = authService.registerRenter(registerRequest);

        return ApiResponse.<AuthResponse>builder()
                .message("Successfully registered user")
                .data(token)
                .build();
    }

    @PostMapping("/login")
    ApiResponse<AuthResponse> loginRenter(@RequestBody LoginRequest loginRequest) {

        var auth = authService.authenticated(loginRequest);
        return ApiResponse.<AuthResponse>builder()
                .data(auth)
                .message("Đăng nhập thành công!f    ")
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logoutRenter(@RequestBody IntrospectRequest introspectRequest)
            throws ParseException, JOSEException {
        return authService.logout(introspectRequest.getToken());
    }

    @PostMapping("/refresh")
    ApiResponse<AuthResponse> refresh(@RequestBody IntrospectRequest introspectRequest)
            throws ParseException, JOSEException {

        var auth = authService.refreshToken(introspectRequest.getToken());
        return ApiResponse.<AuthResponse>builder()
                .data(auth)
                .message("Đăng nhập thành công!f")
                .build();
    }
}
