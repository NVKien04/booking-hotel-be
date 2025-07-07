package com.example.booking_hotel.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booking_hotel.configuration.SecurityUtil;
import com.example.booking_hotel.dto.request.booking.BookingCreateRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.booking.BookingResponse;
import com.example.booking_hotel.entity.Bookings;
import com.example.booking_hotel.entity.Posts;
import com.example.booking_hotel.entity.User;
import com.example.booking_hotel.enums.Booking_status;
import com.example.booking_hotel.exception.AppException;
import com.example.booking_hotel.exception.ErrorCode;
import com.example.booking_hotel.mapper.BookingMapper;
import com.example.booking_hotel.repository.BookingRepository;
import com.example.booking_hotel.repository.PostAvailabilityRepository;
import com.example.booking_hotel.repository.PostRepository;
import com.example.booking_hotel.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {

    PostAvailabilityService postAvailabilityService;
    PostAvailabilityRepository postAvailabilityRepository;
    BookingRepository bookingRepository;
    PostRepository postRepository;
    UserRepository userRepository;
    BookingMapper bookingMapper;
    SecurityUtil securityUtil;
    PriceService priceService;

    @Override
    @Transactional
    public ApiResponse<BookingResponse> createBooking(BookingCreateRequest bookingCreateRequest) {
        validateRequest(bookingCreateRequest);
        Posts post = postRepository
                .findById(bookingCreateRequest.getPostID())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        BigDecimal priceNight = post.getNightPrice();
        BigDecimal priceWeekend = post.getWeekendPrice();
        LocalDate checkIn = bookingCreateRequest.getCheckIn();
        LocalDate checkOut = bookingCreateRequest.getCheckOut();
        if (bookingCreateRequest.getGuest() > post.getCapacity()) {
            throw new AppException(ErrorCode.INVALID_GUEST);
        }
        var userId = securityUtil.getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        if (post.getOwner().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.CANNOT_BOOK_OWN_ROOM);
        }
        // hàm để check nhưng ngày đặt trùng
        postAvailabilityService.getLockDate(
                post.getId(), bookingCreateRequest.getCheckIn(), bookingCreateRequest.getCheckOut());
        var aDays =
                postAvailabilityService.generateAvailability(post.getId(), priceNight, priceWeekend, checkIn, checkOut);
        // chỗ này sao khi tạo các ngày được đặt tính giá theo các ngày đó viết 1 hàm nhận vào các AvaibilityDate rồi
        // tính giá thèo từng ngày rồi go ta ây
        var totalPrice = priceService.calculatePrice(aDays);
        Bookings bookings = bookingMapper.toBooking(bookingCreateRequest);
        bookings.setUser(user);
        bookings.setPosts(post);
        bookings.setStats(Booking_status.DRAFT.getCode());
        bookings.setTotalPrice(totalPrice.getSubtotal());
        bookings.setDiscount(totalPrice.getDiscount());
        bookings.setTotalAmount(totalPrice.getTotalAmount());
        postAvailabilityRepository.saveAll(aDays);
        BookingResponse bookingResponse = bookingMapper.toBookingresponse(bookingRepository.save(bookings));
        bookingResponse.setPostId(post.getId());
        return ApiResponse.<BookingResponse>builder()
                .message("Successfully created booking")
                .data(bookingResponse)
                .build();
    }

    @Override
    public List<LocalDate> getAvailableDate(String postId) {
        List<Bookings> listBooking = bookingRepository.findByPostIdAndStatusIn(
                postId, List.of(Booking_status.CONFIRMED.getCode(), Booking_status.CHECKED_IN.getCode()));
        List<LocalDate> bookListDate = new ArrayList<>();
        for (Bookings booking : listBooking) {
            LocalDate start = booking.getCheckIn();
            LocalDate end = booking.getCheckOut();
            while (start.isBefore(end)) {
                bookListDate.add(start);
                start = start.plusDays(1);
            }
        }
        return bookListDate;
    }

    private void validateRequest(final BookingCreateRequest request) {
        final var checkinDate = request.getCheckIn();
        final var checkoutDate = request.getCheckOut();
        final var currentDate = LocalDate.now();

        if (checkinDate.isBefore(currentDate) || checkinDate.isAfter(checkoutDate)) {
            throw new AppException(ErrorCode.INVALID_DATES);
        }

        if (request.getGuest() <= 0) {
            throw new AppException(ErrorCode.INVALID_DOB);
        }
    }
}
