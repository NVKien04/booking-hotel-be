package com.example.booking_hotel.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.booking_hotel.dto.request.BookingCreateRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.BookingResponse;
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
    public ResponseEntity<ApiResponse<BookingResponse>> create(
            @Valid @RequestBody BookingCreateRequest bookingCreateRequest) {
        var response = bookingService.createBooking(bookingCreateRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<ApiResponse<java.util.List<BookingResponse>>> getMyBookings() {
        return ResponseEntity.ok(bookingService.getMyBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingDetail(@PathVariable String id) {
        return ResponseEntity.ok(bookingService.getBookingDetail(id));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelBooking(@PathVariable String id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    @GetMapping("/host-bookings")
    public ResponseEntity<ApiResponse<java.util.List<BookingResponse>>> getHostBookings() {
        return ResponseEntity.ok(bookingService.getHostBookings());
    }

    @GetMapping("/available-dates/{postId}")
    public ResponseEntity<ApiResponse<java.util.List<java.time.LocalDate>>> getAvailableDates(@PathVariable String postId) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.getAvailableDate(postId)));
    }
}

