package com.example.booking_hotel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.booking_hotel.dto.request.post.PostCreateRequest;
import com.example.booking_hotel.dto.response.post.PostCardItemResponse;
import com.example.booking_hotel.dto.response.post.PostDetailResponse;
import com.example.booking_hotel.dto.response.post.PostResponse;
import com.example.booking_hotel.entity.Posts;

@Mapper(
        componentModel = "spring",
        uses = {Post_ImgMapper.class, AmenitiesMapper.class, ReviewsMapper.class})
public interface PostMapper {
    PostResponse toPostResponse(Posts post);

    PostCardItemResponse toPostCardItemResponse(Posts post);

    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "thumbnail", ignore = true)
    @Mapping(target = "placeType", ignore = true)
    Posts toPosts(PostCreateRequest postCreateRequest);

    //    @Mapping(target = "availableDates", ignore = true)
    //    @Mapping(target = "reviews", ignore = true)
    //    @Mapping(target = "amenities", ignore = true)
    //    @Mapping(target = "post_imagesList", ignore = true)
    ////    @Mapping(target = "user", ignore = true)
    @Mapping(target = "availableDates", ignore = true)
    PostDetailResponse toPostDetailResponse(Posts post);
}
