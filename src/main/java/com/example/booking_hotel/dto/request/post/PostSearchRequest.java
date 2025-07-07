package com.example.booking_hotel.dto.request.post;

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
public class PostSearchRequest {

    String district;
    String city;
    BigDecimal maxPrice;
    BigDecimal minPrice;
    String guest;
    LocalDate startDate;
    LocalDate endDate;
    List<String> amenities;
    String placeType;
}
