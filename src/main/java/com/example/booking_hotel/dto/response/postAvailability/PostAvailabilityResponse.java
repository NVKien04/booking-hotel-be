package com.example.booking_hotel.dto.response.postAvailability;


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
public class PostAvailabilityResponse {
    String id;
    String title;
    String short_description;
    BigDecimal night_price;
    int capacity;
    String thumbnail;
    int rating;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;


}
