package com.example.booking_hotel.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.booking_hotel.configuration.SecurityUtil;
import com.example.booking_hotel.dto.request.UpdateUserRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.AvatarResponse;
import com.example.booking_hotel.dto.response.UserResponse;
import com.example.booking_hotel.service.UserService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;
    SecurityUtil securityUtil;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getInfoUser() {
        var userId = securityUtil.getCurrentUserId();
        log.warn(userId);
        var user = userService.getInfoUser();
        return ResponseEntity.ok(ApiResponse.success("User Info", user));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@RequestBody UpdateUserRequest request) {
        var user = userService.updateUser(request);
        return ResponseEntity.ok(ApiResponse.success("Update User Success", user));
    }


    @PostMapping("/addAvatar")
    public ResponseEntity<ApiResponse<AvatarResponse>> addAvatar(@RequestParam("file") MultipartFile file) {
        var userId = securityUtil.getCurrentUserId();
        log.warn(userId);
        return ResponseEntity.ok(ApiResponse.success("Add Avatar", userService.addAvatar(userId, file)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/fetchAllUser")
    public ResponseEntity<ApiResponse<List<UserResponse>>> fetchAllUser(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int size) {
        var userId = securityUtil.getCurrentUserId();
        log.warn(userId);
        return ResponseEntity.ok(userService.getAllUser(page, size));
    }
}
