package com.example.booking_hotel.mapper;

import org.mapstruct.Mapper;

import com.example.booking_hotel.dto.response.post_img.Post_ImgResponse;
import com.example.booking_hotel.entity.Post_images;

@Mapper
public interface Post_ImgMapper {
    Post_ImgResponse toPost_ImgResponse(Post_images post_images);
}
