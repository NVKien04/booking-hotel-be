package com.example.booking_hotel.dto.request;

import jakarta.persistence.Column;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewRequest {
    int rating;
    String bookingID;

    @Column(columnDefinition = "TEXT")
    String comment;
}
