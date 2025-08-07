package com.example.booking_hotel.service.Impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booking_hotel.configuration.SecurityUtil;
import com.example.booking_hotel.dto.request.ReviewRequest;
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
import com.example.booking_hotel.repository.ReviewRepository;
import com.example.booking_hotel.repository.UserRepository;
import com.example.booking_hotel.service.ReviewService;

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
    BookingRepository bookingRepository;

    @Transactional
    @Override
    public ReviewsResponse createReviews(ReviewRequest reviewRequest) {
        var userId = securityUtil.getCurrentUserId();

        Bookings booking = bookingRepository
                .findById(reviewRequest.getBookingID())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra trạng thái booking và người tạo
        if (!Booking_status.DRAFT.getCode().equals(booking.getStats())
                || !userId.equals(booking.getUser().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Lấy thông tin user và post
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Posts post = postRepository
                .findById(booking.getPosts().getId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        // Tạo review mới
        Reviews review = Reviews.builder()
                .rating(reviewRequest.getRating())
                .comment(reviewRequest.getComment())
                .post(post)
                .user(user)
                .build();
        reviewRepository.save(review);

        // Cập nhật rating trung bình và tổng số review cho post
        int oldTotalReviews = post.getTotalReviews();
        double oldRatingAvg = post.getRating();

        int newRating = reviewRequest.getRating();
        int newTotalReviews = oldTotalReviews + 1;
        double newRatingAvg = ((oldRatingAvg * oldTotalReviews) + newRating) / newTotalReviews;

        post.setTotalReviews(newTotalReviews);
        post.setRating(newRatingAvg);
        postRepository.save(post);

        return reviewsMapper.toReviewsResponse(review);
    }
}
