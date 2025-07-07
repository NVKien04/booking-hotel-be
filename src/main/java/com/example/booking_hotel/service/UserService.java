package com.example.booking_hotel.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.booking_hotel.dto.response.user.AvatarResponse;
import com.example.booking_hotel.dto.response.user.UserResponse;

public interface UserService {

    public UserResponse getInfoUser(String userId);

    public AvatarResponse addAvatar(String idUser, MultipartFile file);
}
