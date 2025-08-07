package com.example.booking_hotel.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.booking_hotel.configuration.SecurityUtil;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.user.AvatarResponse;
import com.example.booking_hotel.dto.response.user.UserResponse;
import com.example.booking_hotel.service.UserService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;
    SecurityUtil securityUtil;

    @GetMapping("/me")
    private ApiResponse<UserResponse> getInfoUser() {
        var userId = securityUtil.getCurrentUserId();
        log.warn(userId);
        var user = userService.getInfoUser();
        return ApiResponse.<UserResponse>builder()
                .data(user)
                .message("User Info")
                .build();
    }

    @PostMapping("/addAvatar")
    private ApiResponse<AvatarResponse> addAvatar(@RequestParam("file") MultipartFile file) {
        var userId = securityUtil.getCurrentUserId();
        log.warn(userId);
        return ApiResponse.<AvatarResponse>builder()
                .message("Add Avatar")
                .data(userService.addAvatar(userId, file))
                .build();
    }



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/fetchAllUser")
    private ApiResponse<List<UserResponse>> fetchAllUser(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int size) {
        var userId = securityUtil.getCurrentUserId();
        log.warn(userId);
        return userService.getAllUser(page, size);
    }
}
