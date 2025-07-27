package com.example.booking_hotel.service;

import com.example.booking_hotel.configuration.SecurityUtil;
import com.example.booking_hotel.dto.request.ReviewRequest;
import com.example.booking_hotel.dto.request.booking.BookingCreateRequest;
import com.example.booking_hotel.dto.response.reviews.ReviewsResponse;
import com.example.booking_hotel.entity.Bookings;
import com.example.booking_hotel.entity.Posts;
import com.example.booking_hotel.entity.Reviews;
import com.example.booking_hotel.entity.User;
import com.example.booking_hotel.enums.Booking_status;
import com.example.booking_hotel.exception.AppException;
import com.example.booking_hotel.exception.ErrorCode;
import com.example.booking_hotel.mapper.ReviewsMapper;
import com.example.booking_hotel.repository.BookingRepository;
import com.example.booking_hotel.repository.PostRepository;
import com.example.booking_hotel.repository.UserRepository;
import org.springframework.stereotype.Service;

import com.example.booking_hotel.repository.ReviewRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewServiceImpl implements ReviewService {
    ReviewRepository reviewRepository;
    ReviewsMapper reviewsMapper;
    SecurityUtil securityUtil;
    UserRepository userRepository;
    PostRepository postRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ReviewsResponse createReviews(ReviewRequest reviewRequest) {
        var userId = securityUtil.getCurrentUserId();//người hiện tại đang đăng nhập
        Bookings bookings = bookingRepository.findById(reviewRequest.getBookingID()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(!bookings.getStats().equals(Booking_status.DRAFT .getCode())){
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        };
        if(!userId.equals(bookings.getUser().getId())){
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Posts post = postRepository
                .findById(bookings.getPosts().getId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        Reviews reviews = Reviews.builder()
                .rating(reviewRequest.getRating())
                .comment(reviewRequest.getComment())
                .post(post)
                .user(user)
                .build();
        reviewRepository.save(reviews);

        return reviewsMapper.toReviewsResponse(reviewRepository.save(reviews));
    }
}
