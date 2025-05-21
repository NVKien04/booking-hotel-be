package com.example.booking_hotel.mapper;

import com.example.booking_hotel.dto.request.auth.RegisterRequest;
import com.example.booking_hotel.dto.response.user.UserResponse;
import com.example.booking_hotel.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper     {
    User mapToUser(RegisterRequest registerRequest);

    UserResponse mapToUserResponse(User user);

}
