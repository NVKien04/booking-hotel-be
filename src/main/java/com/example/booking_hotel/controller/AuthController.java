package com.example.booking_hotel.controller;


import com.example.booking_hotel.dto.request.auth.LoginRequest;
import com.example.booking_hotel.dto.request.auth.RegisterRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.auth.AuthResponse;
import com.example.booking_hotel.dto.response.user.UserResponse;
import com.example.booking_hotel.service.AuthService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping("/register")
    ApiResponse<UserResponse> registerRenter(@RequestBody RegisterRequest registerRequest) {
        var user = authService.registerRenter(registerRequest);

        return ApiResponse.<UserResponse>builder()
                .message("Successfully registered user")
                .data(user)
                .build();
    }


    @PostMapping("/login")
    ApiResponse<AuthResponse> loginRenter(@RequestBody LoginRequest loginRequest) {
        var auth = authService.authenticated(loginRequest);
        return ApiResponse.<AuthResponse>builder()
                .data(auth)
                .message("Successfully logged in user")
                .build();

    }




}
