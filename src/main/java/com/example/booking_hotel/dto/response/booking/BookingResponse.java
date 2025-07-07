package com.example.booking_hotel.dto.response.booking;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.example.booking_hotel.dto.response.reviews.ReviewsResponse;
import com.example.booking_hotel.dto.response.user.UserResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingResponse {
    UserResponse user;
    LocalDate check_in;
    LocalDate check_out;
    BigDecimal totalPrice;
    BigDecimal discount;
    BigDecimal totalAmount;
    int guest;
    List<ReviewsResponse> reviews;
    String postId;
}
