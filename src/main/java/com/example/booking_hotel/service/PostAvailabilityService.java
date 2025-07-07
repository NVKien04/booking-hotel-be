package com.example.booking_hotel.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.example.booking_hotel.entity.PostsAvailability;

public interface PostAvailabilityService {

    List<PostsAvailability> generateAvailability(
            String postId, BigDecimal defaultPrice, BigDecimal weekendPrice, LocalDate checkIn, LocalDate checkOut);

    List<PostsAvailability> getLockDate(String postId, LocalDate checkIn, LocalDate checkOut);

    List<LocalDate> getLockDateList(String postId);
}
