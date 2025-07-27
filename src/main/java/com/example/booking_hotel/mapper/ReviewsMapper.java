package com.example.booking_hotel.mapper;

import com.example.booking_hotel.dto.request.ReviewRequest;
import org.mapstruct.Mapper;

import com.example.booking_hotel.dto.response.reviews.ReviewsResponse;
import com.example.booking_hotel.entity.Reviews;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ReviewsMapper {
    ReviewsResponse toReviewsResponse(Reviews reviews);

}
