package com.example.booking_hotel.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booking_hotel.constant.CookieEnum;
import com.example.booking_hotel.dto.request.*;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.AuthResponse;
import com.example.booking_hotel.dto.response.UserResponse;
import com.example.booking_hotel.exception.AppException;
import com.example.booking_hotel.exception.ErrorCode;
import com.example.booking_hotel.service.AuthService;
import com.example.booking_hotel.service.EmailService;
import com.example.booking_hotel.service.UserService;
import com.example.booking_hotel.utils.CookieHelper;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;
    EmailService emailService;
    UserService userService;
    CookieHelper cookieHelper;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> registerRenter(
            @Valid @RequestBody RegisterRequest registerRequest,
            HttpServletResponse response) {

        var auth = authService.registerRenter(registerRequest);
        cookieHelper.setAuthCookies(response, auth.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.success("Successfully registered user", sanitizeResponse(auth)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> loginRenter(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {

        var auth = authService.authenticated(loginRequest);
        cookieHelper.setAuthCookies(response, auth.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công!", sanitizeResponse(auth)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logoutRenter(
            @RequestBody(required = false) LogoutTokenRequest logoutTokenRequest,
            HttpServletResponse response)
            throws ParseException, JOSEException {

        ApiResponse<Void> res;
        if (logoutTokenRequest != null && logoutTokenRequest.getAccessToken() != null) {
            res = authService.logout(logoutTokenRequest);
        } else {
            res = ApiResponse.<Void>builder().message("logged out successfully").build();
        }

        cookieHelper.clearAuthCookies(response);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @CookieValue(name = CookieEnum.REFRESH_TOKEN_COOKIE, required = false) String refreshTokenCookie,
            @RequestBody(required = false) IntrospectRequest introspectRequest,
            HttpServletResponse response)
            throws ParseException, JOSEException {

        String token = refreshTokenCookie;
        if ((token == null || token.isBlank()) && introspectRequest != null) {
            token = introspectRequest.getToken();
        }
        if (token == null || token.isBlank()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var auth = authService.refreshToken(token);
        cookieHelper.setAuthCookies(response, auth.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success("Refresh token thành công", sanitizeResponse(auth)));
    }

    @PostMapping("/outbound/authentication")
    public ResponseEntity<ApiResponse<AuthResponse>> outboundAuthentication(
            @RequestParam("code") String code,
            HttpServletResponse response) {

        var auth = authService.outboundAuthenticate(code);
        cookieHelper.setAuthCookies(response, auth.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.success(sanitizeResponse(auth)));
    }

    @GetMapping("/testEmail")
    public ResponseEntity<ApiResponse<Void>> testEmail(@RequestParam("email") String email) {

        Map<String, Object> model = new HashMap<>();
        emailService.sendEmail(
                "nguyenvankien2004hanam@gmail.com", "Chúc mừng! Đăng ký DevJob thành công", model, "register");

        return ResponseEntity.ok(ApiResponse.success("success"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<UserResponse>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.ChangePassword(request);
        return ResponseEntity.ok(ApiResponse.success("Đổi mật khẩu thành công", userService.getInfoUser()));
    }

    private AuthResponse sanitizeResponse(AuthResponse authResponse) {

        return AuthResponse.builder()
                .accessToken(authResponse.getAccessToken())
                .tokenType(authResponse.getTokenType() != null ? authResponse.getTokenType() : "Bearer")
                .expiresIn(authResponse.getExpiresIn())
                .authenticated(true)
                .build();
    }

}
