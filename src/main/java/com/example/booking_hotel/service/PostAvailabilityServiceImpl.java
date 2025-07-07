package com.example.booking_hotel.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.booking_hotel.entity.*;
import com.example.booking_hotel.enums.Lock_status;
import com.example.booking_hotel.exception.AppException;
import com.example.booking_hotel.exception.ErrorCode;
import com.example.booking_hotel.repository.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostAvailabilityServiceImpl implements PostAvailabilityService {

    static final int NIGHT_MAX = 365;
    PostRepository postRepository;
    PostAvailabilityRepository postAvailabilityRepository;

    @Override
    public List<PostsAvailability> generateAvailability(
            String postId, BigDecimal defaultPrice, BigDecimal weekendPrice, LocalDate checkIn, LocalDate checkOut) {

        if (checkIn == null || checkOut == null || !checkIn.isBefore(checkOut)) {
            throw new AppException(ErrorCode.INVALID_DATES);
        }
        Posts posts = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        List<PostsAvailability> availabilityList = new ArrayList<>();
        long days = ChronoUnit.DAYS.between(checkIn, checkOut);

        for (int i = 0; i < days; i++) {
            LocalDate date = checkIn.plusDays(i);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            boolean isWeekend = (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);

            BigDecimal price = (isWeekend && weekendPrice != null && weekendPrice.compareTo(BigDecimal.ZERO) > 0)
                    ? weekendPrice
                    : defaultPrice;
            PostsAvailability availability = new PostsAvailability();
            availability.setDate(date);
            availability.setPrice(price);
            availability.setStatus(Lock_status.DRAFT.getCode());
            availability.setPost(posts);
            availabilityList.add(availability);
        }
        return availabilityList;
    }

    @Override
    public List<PostsAvailability> getLockDate(String postId, LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights > NIGHT_MAX) {
            throw new AppException(ErrorCode.ROOM_ALREADY_BOOKED);
        }
        List<PostsAvailability> aDays = postAvailabilityRepository.findLockPtAvailability(
                postId, Lock_status.DRAFT.getCode(), checkIn, checkOut.minusDays(1));
        if (!aDays.isEmpty()) {
            throw new AppException(ErrorCode.ROOM_ALREADY_BOOKED);
        }
        return aDays;
    }

    @Override
    public List<LocalDate> getLockDateList(String postId) {
        List<PostsAvailability> aDays =
                postAvailabilityRepository.findLockPtAvailabilityList(postId, Lock_status.DRAFT.getCode());
        return aDays.stream().map(PostsAvailability::getDate).sorted().collect(Collectors.toList());
    }
}
