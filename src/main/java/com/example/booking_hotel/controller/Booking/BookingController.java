package com.example.booking_hotel.controller.Booking;

import org.springframework.web.bind.annotation.*;

import com.example.booking_hotel.dto.request.booking.BookingCreateRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.booking.BookingResponse;
import com.example.booking_hotel.service.BookingService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/book")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {

    BookingService bookingService;

    @PostMapping("/room")
    public ApiResponse<BookingResponse> create(@RequestBody BookingCreateRequest bookingCreateRequest) {
        return bookingService.createBooking(bookingCreateRequest);
    }
}
