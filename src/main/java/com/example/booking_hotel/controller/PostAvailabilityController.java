package com.example.booking_hotel.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.service.PostAvailabilityService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@AllArgsConstructor
@RequestMapping("/availability")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostAvailabilityController {
    PostAvailabilityService postAvailabilityService;

    @GetMapping("/locked-date/{postId}")
    public ApiResponse<List<LocalDate>> getSelectDate(@PathVariable String postId) {
        return ApiResponse.<List<LocalDate>>builder()
                .message("Success")
                .data(postAvailabilityService.getLockDateList(postId))
                .build();
    }
}
