package com.example.booking_hotel.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.booking_hotel.dto.request.ReviewRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.reviews.ReviewsResponse;
import com.example.booking_hotel.service.ReviewService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@AllArgsConstructor
@RequestMapping("/review")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewService reviewService;

    @PostMapping("/create")
    public ApiResponse<ReviewsResponse> createReview(@Valid @RequestBody ReviewRequest reviewRequest) {

        return ApiResponse.<ReviewsResponse>builder()
                .message("success")
                .data(reviewService.createReviews(reviewRequest))
                .build();
    }
}
