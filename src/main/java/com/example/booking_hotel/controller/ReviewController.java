package com.example.booking_hotel.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booking_hotel.dto.request.ReviewRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.ReviewsResponse;
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
    public ResponseEntity<ApiResponse<ReviewsResponse>> createReview(@Valid @RequestBody ReviewRequest reviewRequest) {
        var response = reviewService.createReviews(reviewRequest);
        return ResponseEntity.ok(ApiResponse.success("success", response));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<java.util.List<ReviewsResponse>>> getReviewsByPost(
            @PathVariable String postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reviewService.getReviewsByPostId(postId, page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable String id) {
        return ResponseEntity.ok(reviewService.deleteReview(id));
    }
}

