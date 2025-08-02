package com.example.booking_hotel.dto.request.booking;

import java.time.Instant;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingCreateRequest {
    LocalDate checkIn;
    LocalDate checkOut;

    @Min(value = 0, message = "guest phải lớn hơn 0")
    @NotNull(message = "guest không được null")
    int guest;

    @NotBlank(message = "postID không được để trống")
    String postID;
}
