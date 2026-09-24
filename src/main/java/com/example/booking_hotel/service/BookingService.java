package com.example.booking_hotel.service;

import java.time.LocalDate;
import java.util.List;

import com.example.booking_hotel.dto.request.BookingCreateRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.BookingResponse;

public interface BookingService {

    ApiResponse<BookingResponse> createBooking(BookingCreateRequest bookingCreateRequest);

    List<LocalDate> getAvailableDate(String postId);
}
