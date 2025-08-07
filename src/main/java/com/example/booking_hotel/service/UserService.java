package com.example.booking_hotel.service;

import com.example.booking_hotel.dto.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import com.example.booking_hotel.dto.response.user.AvatarResponse;
import com.example.booking_hotel.dto.response.user.UserResponse;

import java.util.List;

public interface UserService {

    public UserResponse getInfoUser();

    public ApiResponse<List<UserResponse>> getAllUser(int page, int size);

    public AvatarResponse addAvatar(String idUser, MultipartFile file);
}
