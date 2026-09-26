package com.example.booking_hotel.service.Impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booking_hotel.configuration.SecurityUtil;
import com.example.booking_hotel.dto.request.BookingCreateRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.BookingResponse;
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
import com.example.booking_hotel.service.BookingService;
import com.example.booking_hotel.service.PostAvailabilityService;

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
    VnPayService vnPayService;

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
        bookings = bookingRepository.save(bookings);
        for (var aDay : aDays) {
            aDay.setBooking(bookings);
        }
        postAvailabilityRepository.saveAll(aDays);
        BookingResponse bookingResponse = bookingMapper.toBookingresponse(bookings);
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

    @Override
    public ApiResponse<List<BookingResponse>> getMyBookings() {
        String userId = securityUtil.getCurrentUserId();
        List<Bookings> list = bookingRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<BookingResponse> responses = list.stream()
                .map(bookingMapper::toBookingresponse)
                .toList();
        return ApiResponse.<List<BookingResponse>>builder()
                .message("Lấy danh sách đặt phòng thành công")
                .data(responses)
                .build();
    }

    @Override
    public ApiResponse<BookingResponse> getBookingDetail(String id) {
        String userId = securityUtil.getCurrentUserId();
        Bookings booking = bookingRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
        boolean isOwner = booking.getPosts() != null
                && booking.getPosts().getOwner() != null
                && userId.equals(booking.getPosts().getOwner().getId());
        boolean isBooker = booking.getUser() != null && userId.equals(booking.getUser().getId());
        if (!isBooker && !isOwner) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        return ApiResponse.<BookingResponse>builder()
                .message("Lấy chi tiết đặt phòng thành công")
                .data(bookingMapper.toBookingresponse(booking))
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<Void> cancelBooking(String id) {
        String userId = securityUtil.getCurrentUserId();
        Bookings booking = bookingRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
        if (booking.getUser() == null || !userId.equals(booking.getUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if (Booking_status.CHECKED_IN.getCode().equals(booking.getStats())
                || Booking_status.CHECKED_OUT.getCode().equals(booking.getStats())
                || Booking_status.COMPLETED.getCode().equals(booking.getStats())
                || Booking_status.CANCELLED.getCode().equals(booking.getStats())) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }
        booking.setStats(Booking_status.CANCELLED.getCode());
        bookingRepository.save(booking);

        if (booking.getPostsAvailabilities() != null && !booking.getPostsAvailabilities().isEmpty()) {
            postAvailabilityRepository.deleteAll(booking.getPostsAvailabilities());
        }

        return ApiResponse.<Void>builder()
                .message("Hủy đặt phòng thành công")
                .build();
    }

    @Override
    public ApiResponse<List<BookingResponse>> getHostBookings() {
        String userId = securityUtil.getCurrentUserId();
        List<Bookings> list = bookingRepository.findByPostsOwnerIdOrderByCreatedAtDesc(userId);
        List<BookingResponse> responses = list.stream()
                .map(bookingMapper::toBookingresponse)
                .toList();
        return ApiResponse.<List<BookingResponse>>builder()
                .message("Lấy danh sách đặt phòng của chủ nhà thành công")
                .data(responses)
                .build();
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
