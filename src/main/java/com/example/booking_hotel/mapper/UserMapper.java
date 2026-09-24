package com.example.booking_hotel.mapper;

import org.mapstruct.Mapper;

import com.example.booking_hotel.dto.request.RegisterRequest;
import com.example.booking_hotel.dto.response.UserResponse;
import com.example.booking_hotel.entity.User;

@Mapper
public interface UserMapper {
    User mapToUser(RegisterRequest registerRequest);

    UserResponse mapToUserResponse(User user);
}
