package com.example.booking_hotel.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.AvatarResponse;
import com.example.booking_hotel.dto.response.UserResponse;

public interface UserService {

    public UserResponse getInfoUser();

    public ApiResponse<List<UserResponse>> getAllUser(int page, int size);

    public AvatarResponse addAvatar(String idUser, MultipartFile file);
}
