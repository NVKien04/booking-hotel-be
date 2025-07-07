package com.example.booking_hotel.dto.response.post;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
    BigDecimal nightPrice;
    BigDecimal weekendPrice;
    String fullAddress;
    int capacity;
    String thumbnail;
    int rating;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
