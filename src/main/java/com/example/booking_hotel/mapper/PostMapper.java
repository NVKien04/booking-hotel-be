package com.example.booking_hotel.mapper;

import com.example.booking_hotel.dto.request.PostCreateRequest;
import com.example.booking_hotel.dto.response.PostResponse;
import com.example.booking_hotel.entity.Posts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PostMapper {
    PostResponse toPostResponse(Posts post);

    @Mapping(target = "accommodation_type", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "thumbnail", ignore = true)
    Posts toPosts(PostCreateRequest postCreateRequest);

}
