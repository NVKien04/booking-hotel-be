package com.example.booking_hotel.service.Impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booking_hotel.configuration.SecurityUtil;
import com.example.booking_hotel.dto.request.ReviewRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.Pagination;
import com.example.booking_hotel.dto.response.ReviewsResponse;
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

    @Override
    public ApiResponse<List<ReviewsResponse>> getReviewsByPostId(String postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reviews> reviewPage = reviewRepository.findByPostId(postId, pageable);

        List<ReviewsResponse> reviewsResponses = reviewPage.getContent().stream()
                .map(reviewsMapper::toReviewsResponse)
                .toList();

        Pagination pagination = Pagination.builder()
                .page(page)
                .limit(size)
                .totalPages(reviewPage.getTotalPages())
                .totalRecords(reviewPage.getTotalElements())
                .build();

        return ApiResponse.<List<ReviewsResponse>>builder()
                .code(1)
                .message("success")
                .data(reviewsResponses)
                .pagination(pagination)
                .build();
    }

    @Transactional
    @Override
    public ApiResponse<Void> deleteReview(String id) {
        String userId = securityUtil.getCurrentUserId();
        Reviews review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean isOwner = review.getUser().getId().equals(userId);
        boolean isAdmin = currentUser.getRole() != null && currentUser.getRole().name().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Posts post = review.getPost();
        if (post != null) {
            int oldTotalReviews = post.getTotalReviews();
            double oldRatingAvg = post.getRating();
            if (oldTotalReviews <= 1) {
                post.setTotalReviews(0);
                post.setRating(0.0);
            } else {
                int newTotalReviews = oldTotalReviews - 1;
                double newRatingAvg = ((oldRatingAvg * oldTotalReviews) - review.getRating()) / newTotalReviews;
                post.setTotalReviews(newTotalReviews);
                post.setRating(Math.max(0.0, newRatingAvg));
            }
            postRepository.save(post);
        }

        reviewRepository.delete(review);

        return ApiResponse.<Void>builder()
                .code(1)
                .message("Delete review successfully")
                .build();
    }
}

