package com.example.booking_hotel.controller;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import com.example.booking_hotel.dto.request.BookingCreateRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.BookingResponse;
import com.example.booking_hotel.service.BookingService;
import com.example.booking_hotel.service.PostAvailabilityService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/availability")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostAvailabilityController {

    SimpMessagingTemplate messagingTemplate;
    PostAvailabilityService postAvailabilityService;
    BookingService bookingService;

    public void notifyLockedDatesChanged(String postId) {
        List<LocalDate> dates = postAvailabilityService.getLockDateList(postId);
        messagingTemplate.convertAndSend("/topic/locked-date/" + postId, dates);
    }

    @PostMapping("/book-room")
    public ResponseEntity<ApiResponse<BookingResponse>> bookRoom(@Valid @RequestBody BookingCreateRequest req) {
        var rs = bookingService.createBooking(req);
        notifyLockedDatesChanged(req.getPostID());
        return ResponseEntity.ok(rs);
    }

    @GetMapping("/locked-date/{postId}")
    public ResponseEntity<ApiResponse<List<LocalDate>>> getLockedDates(@Valid @PathVariable String postId) {
        return ResponseEntity.ok(ApiResponse.success(postAvailabilityService.getLockDateList(postId)));
    }

    @MessageMapping("/request-locked-date")
    public void handleRequest(String postId) {
        notifyLockedDatesChanged(postId);
    }
}
