package com.example.booking_hotel.dto.request.booking;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingCreateRequest {
    LocalDate checkIn;
    LocalDate checkOut;
    int guest;
    String postID;
}
