package com.example.booking_hotel.controller;

import com.example.booking_hotel.dto.request.ReviewRequest;
import com.example.booking_hotel.dto.request.auth.IntrospectRequest;
import com.example.booking_hotel.dto.request.auth.LoginRequest;
import com.example.booking_hotel.dto.request.auth.RegisterRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.auth.AuthResponse;
import com.example.booking_hotel.dto.response.reviews.ReviewsResponse;
import com.example.booking_hotel.service.AuthService;
import com.example.booking_hotel.service.ReviewService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

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
