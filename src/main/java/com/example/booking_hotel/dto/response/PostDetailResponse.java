package com.example.booking_hotel.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDetailResponse {
    String id;
    String title;
    String short_description;
    BigDecimal nightPrice;
    BigDecimal weekendPrice;
    int capacity;
    List<Post_ImgResponse> post_imagesList;
    List<AmenitiesResponse> amenities;
    UserResponse owner;
    List<ReviewsResponse> reviews;
    int rating;
    int totalReviews;
    List<LocalDate> availableDates;
    String fullAddress;
    int bedrooms;
    int bathrooms;
    int beds;
}
