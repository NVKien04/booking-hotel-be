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
public class PostResponse {
    String id;
    String title;
    String short_description;
    BigDecimal night_price;
    int capacity;
    String thumbnail;
    int rating;
    String city;
    String country;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;


}
