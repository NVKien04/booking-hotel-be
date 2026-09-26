package com.example.booking_hotel.service;

import java.util.List;

import com.example.booking_hotel.dto.request.ReviewRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.ReviewsResponse;

public interface ReviewService {

    ReviewsResponse createReviews(ReviewRequest reviewRequest);

    ApiResponse<List<ReviewsResponse>> getReviewsByPostId(String postId, int page, int size);

    ApiResponse<Void> deleteReview(String id);
}

