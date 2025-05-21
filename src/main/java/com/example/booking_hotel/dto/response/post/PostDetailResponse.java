package com.example.booking_hotel.dto.response.post;


import com.example.booking_hotel.entity.Reviews;
import com.example.booking_hotel.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDetailResponse {
    String id;
    String title;
    String description;
    String short_description;
    BigDecimal night_price;
    int capacity;
    List<String>  img_urls = new ArrayList<>();
    List<String>  amenities = new ArrayList<>();
    User user = new User();
    List<Reviews> reviews = new ArrayList<>();
    int rating;
    int totalReviews;
    String city;
    String country;
    List<LocalDateTime> availableDates;


}
