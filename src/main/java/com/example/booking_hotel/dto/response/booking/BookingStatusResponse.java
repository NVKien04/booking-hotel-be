package com.example.booking_hotel.dto.response.booking;


import com.example.booking_hotel.dto.response.post.PostDetailResponse;
import com.example.booking_hotel.dto.response.post.PostResponse;
import com.example.booking_hotel.dto.response.reviews.ReviewsResponse;
import com.example.booking_hotel.dto.response.user.UserResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingResponse {
    UserResponse user;
    Date check_in;
    Date check_out;
    BigDecimal totalPrice;
    int numberOfAdults;
    int numberOfChildren;
    List<ReviewsResponse> reviews;
    String postId;
}
