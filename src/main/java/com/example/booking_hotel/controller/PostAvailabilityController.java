package com.example.booking_hotel.controller;

import java.time.LocalDate;
import java.util.List;

import com.example.booking_hotel.dto.request.booking.BookingCreateRequest;
import com.example.booking_hotel.dto.response.booking.BookingResponse;
import com.example.booking_hotel.service.BookingService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import com.example.booking_hotel.dto.response.ApiResponse;
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
    public ApiResponse<BookingResponse> bookRoom(@RequestBody BookingCreateRequest req) {
        var rs = bookingService.createBooking(req);
        notifyLockedDatesChanged(req.getPostID());
        return rs;
    }

    @GetMapping("/locked-date/{postId}")
    public ApiResponse<List<LocalDate>> getLockedDates(@PathVariable String postId) {
        return ApiResponse.<List<LocalDate>>builder()
                .message("Success")
                .data(postAvailabilityService.getLockDateList(postId))
                .build();
    }

    @MessageMapping("/request-locked-date")
    public void handleRequest(String postId) {
        notifyLockedDatesChanged(postId);
    }
}
