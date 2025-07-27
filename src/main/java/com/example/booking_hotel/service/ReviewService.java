package com.example.booking_hotel.service;

import com.example.booking_hotel.dto.request.ReviewRequest;
import com.example.booking_hotel.dto.response.reviews.ReviewsResponse;

public interface ReviewService {

    public ReviewsResponse createReviews(ReviewRequest reviewRequest);
}
