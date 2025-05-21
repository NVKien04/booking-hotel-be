package com.example.booking_hotel.mapper;

import com.example.booking_hotel.dto.request.auth.RegisterRequest;
import com.example.booking_hotel.dto.response.post.Post_ImgResponse;
import com.example.booking_hotel.dto.response.user.UserResponse;
import com.example.booking_hotel.entity.Post_images;
import com.example.booking_hotel.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface Post_ImgMapper {
    Post_ImgResponse toPost_ImgResponse(Post_images post_images);

}
