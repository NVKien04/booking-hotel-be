package com.example.booking_hotel.dto.response.post;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostCardItemResponse {
    String id;
    String title;
    BigDecimal night_price;
    String short_description;
    String thumbnail;
    int rating;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String city;
    String country;


}
