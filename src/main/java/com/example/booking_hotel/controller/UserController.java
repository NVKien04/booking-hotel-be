package com.example.booking_hotel.controller;

import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.user.AvatarResponse;
import com.example.booking_hotel.dto.response.user.UserResponse;
import com.example.booking_hotel.service.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    private ApiResponse<AvatarResponse> addAvatar(@RequestParam String userId, @RequestParam("file") MultipartFile file) {
        return ApiResponse.<AvatarResponse>builder()
                .message("Add Avatar")
                .data(userService.addAvatar(userId, file))
                .build();
    }


    @GetMapping("/user/{avatar_url}")
    private ResponseEntity<Resource> thumbnail(@PathVariable String avatar_url) {
        try{
            Path filePath = Paths.get("uploads/user").resolve(avatar_url).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            String contentType = Files.probeContentType(filePath);
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        }
        catch (MalformedURLException e){
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
