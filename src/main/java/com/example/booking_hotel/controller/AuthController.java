package com.example.booking_hotel.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.example.booking_hotel.dto.request.auth.*;
import com.example.booking_hotel.dto.response.TokenResponse;
import com.example.booking_hotel.dto.response.VerificationCodeResponse;
import com.example.booking_hotel.dto.response.user.UserResponse;
import com.example.booking_hotel.service.EmailService;
import com.example.booking_hotel.service.UserService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    EmailService emailService;
    UserService userService;

    @PostMapping("/register")
    ApiResponse<TokenResponse> registerRenter(@Valid @RequestBody RegisterRequest registerRequest) {
        var token = authService.registerRenter(registerRequest);
        return ApiResponse.<TokenResponse>builder()
                .message("Successfully registered user")
                .data(token)
                .build();
    }

    @PostMapping("/login")
    ApiResponse<TokenResponse> loginRenter(@Valid @RequestBody LoginRequest loginRequest) {
        return ApiResponse.<TokenResponse>builder()
                .data( authService.authenticated(loginRequest))
                .message("Đăng nhập thành công!f    ")
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logoutRenter(@Valid @RequestBody LogoutTokenRequest logoutTokenRequest)
            throws ParseException, JOSEException {
        return authService.logout(logoutTokenRequest);
    }

    @PostMapping("/refresh")
    ApiResponse<TokenResponse> refresh(@Valid @RequestBody IntrospectRequest introspectRequest)
            throws ParseException, JOSEException {

        var auth = authService.refreshToken(introspectRequest.getToken());
        return ApiResponse.<TokenResponse>builder()
                .data(auth)
                .message("Đăng nhập thành công!f")
                .build();
    }

    @PostMapping("/outbound/authentication")
    ApiResponse<TokenResponse> outboundAuthentication(@RequestParam("code") String code) {
        return ApiResponse.<TokenResponse>builder()
                .data(authService.outboundAuthenticate(code))
                .message("Success")
                .build();
    }

    @GetMapping("/testEmail")
    ApiResponse<Void> testEmail(@RequestParam("email") String email) {
        Map<String, Object> model = new HashMap<>();

        emailService.sendEmail("nguyenvankien2004hanam@gmail.com", "Chúc mừng! Đăng ký DevJob thành công", model, "register");
        return ApiResponse.<Void>builder()
                .message("success")
                .build();
    }

    @PostMapping("/change-password")
    public ApiResponse<UserResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.ChangePassword(request);
        return ApiResponse.<UserResponse>builder()
                .data(userService.getInfoUser())
                .message("My Info")
                .build();
    }


//    @PostMapping("/forgot-password")
//    public ApiResponse<VerificationCodeResponse> forgotPassword(@Valid @RequestBody EmailRequest request) {
//        return ApiResponse.<VerificationCodeResponse>builder()
//                .data()
//                .message("Mã xác nhận đã được gửi vào email của bạn")
//                .build();
//    }

}
