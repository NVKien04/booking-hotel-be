package com.example.booking_hotel.mapper;

import org.mapstruct.Mapper;

import com.example.booking_hotel.dto.response.reviews.ReviewsResponse;
import com.example.booking_hotel.entity.Reviews;

@Mapper
public interface ReviewsMapper {
    ReviewsResponse toReviewsResponse(Reviews reviews);
}
