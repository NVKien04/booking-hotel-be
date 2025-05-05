package com.example.booking_hotel.mapper;

import com.example.booking_hotel.dto.request.RegisterRequest;
import com.example.booking_hotel.dto.response.UserResponse;
import com.example.booking_hotel.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper     {
    User mapToUser(RegisterRequest registerRequest);

    UserResponse mapToUserResponse(User user);

}
