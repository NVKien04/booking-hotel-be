package com.example.booking_hotel.controller;

import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.AvatarResponse;
import com.example.booking_hotel.dto.response.UserResponse;
import com.example.booking_hotel.service.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @GetMapping("/info")
    private ApiResponse<UserResponse> getInfoUser(@RequestBody String userId) {
        var user = userService.getInfoUser(userId);

        return ApiResponse.<UserResponse>builder()
                .data(user)
                .message("User Info")
                .build();

    }

    @PostMapping("/addAvatar")
    private ApiResponse<AvatarResponse> addAvatar(@RequestBody String userId, @RequestParam("file") MultipartFile file) {
        return ApiResponse.<AvatarResponse>builder()
                .message("Add Avatar")
                .data(userService.addAvatar(userId, file))
                .build();
    }


}
