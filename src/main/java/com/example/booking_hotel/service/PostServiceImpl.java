package com.example.booking_hotel.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.booking_hotel.entity.*;
import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booking_hotel.configuration.SecurityUtil;
import com.example.booking_hotel.dto.request.post.PostCreateRequest;
import com.example.booking_hotel.dto.request.post.PostSearchRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.Pagination;
import com.example.booking_hotel.dto.response.post.PostCardItemResponse;
import com.example.booking_hotel.dto.response.post.PostDetailResponse;
import com.example.booking_hotel.dto.response.post.PostResponse;
import com.example.booking_hotel.exception.AppException;
import com.example.booking_hotel.exception.ErrorCode;
import com.example.booking_hotel.mapper.PostMapper;
import com.example.booking_hotel.repository.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {

    @NonFinal
    @Value("${file.upload-post}")
    String thumbnailPath;
    PostRepository postRepository;
    BookingService bookingService;
    UserRepository userRepository;
    AmenitiesRepository amenitiesRepository;
    Place_TypeRepository placeTypeRepository;
    PostAvailabilityService postAvailabilityService;
    Post_imgService postImgService;
    SecurityUtil securityUtil;
    UploadService uploadService;
    PostMapper postMapper;
    CityRepository cityRepository;
    DistrictRepository districtRepository;
    @Override
    @Transactional
    public PostResponse create(PostCreateRequest request) {
        String userId = securityUtil.getCurrentUserId();

        // Lấy thông tin user
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Mapping request sang entity
        Posts post = postMapper.toPosts(request);
        post.setOwner(owner);

        City city = cityRepository.findById(request.getCityId()).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        District district = districtRepository.findById(request.getDistrictId()).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        post.setCity(city);
        post.setDistrict(district);

        // Xử lý địa chỉ đầy đủ
        String fullAddress = Stream.of(request.getAddressDetail(), district.getName(), city.getName())
                .filter(Objects::nonNull)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining(", "));
        post.setFullAddress(fullAddress);

        // Upload ảnh thumbnail
        String thumbnailUrl = uploadService.uploadFile(request.getThumbnail(), thumbnailPath, "post");
        post.setThumbnail(thumbnailUrl);

        // Gán loại chỗ ở
        placeTypeRepository.findById(request.getPlaceType())
                .ifPresent(post::setPlaceType);

        // Gán tiện ích (amenities)
        Set<Amenities> amenities = new HashSet<>(amenitiesRepository.findAllById(request.getAmenityIds()));
        post.setAmenities(amenities);

        // Lưu bài đăng
        Posts savedPost = postRepository.save(post);

        // Upload ảnh nhiều
        postImgService.uploadMultipleImg_Post(request.getFiles(), savedPost.getId());

        return postMapper.toPostResponse(savedPost);
    }


    @Override
    public ApiResponse<List<PostCardItemResponse>> search(int page, int size, String sort, PostSearchRequest search) {
        Sort sortable = Sort.by("rating").ascending();
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            String sortField = sortParams[0];
            String sortDirection = sortParams.length > 1 ? sortParams[1] : "asc";
            sortable = sortDirection.equalsIgnoreCase("desc")
                    ? Sort.by(sortField).descending()
                    : Sort.by(sortField).ascending();
        }
        Pageable pageable = PageRequest.of(page, size, sortable);
        Page<Posts> pagePosts = postRepository.filterRoom(
                search.getCity_slug(),
                search.getMaxPrice(),
                search.getMinPrice(),
                search.getStartDate(),
                search.getEndDate(),
                search.getAmenities(),
                search.getPlaceType(),
                pageable);
        List<Posts> listPost = pagePosts.getContent();
        var pagination = Pagination.builder()
                .page(page)
                .limit(size)
                .totalPages(pagePosts.getTotalPages())
                .totalRecords(pagePosts.getTotalElements())
                .build();
        List<PostCardItemResponse> listPostCardItemResponse =
                listPost.stream().map(postMapper::toPostCardItemResponse).toList();

        return ApiResponse.<List<PostCardItemResponse>>builder()
                .message("Success")
                .data(listPostCardItemResponse)
                .pagination(pagination)
                .build();
    }
    @Override
    public ApiResponse<PostDetailResponse> getPostDetail(String id) {
        Posts post = postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        var postDetail = postMapper.toPostDetailResponse(post);
        postDetail.setAvailableDates(postAvailabilityService.getLockDateList(id));
        return ApiResponse.<PostDetailResponse>builder()
                .code(1)
                .message("success")
                .data(postDetail)
                .build();
    }

    @Override
    public List<LocalDate> getSelectDates(String id) {
        return bookingService.getAvailableDate(id);
    }

    @Override
    public ApiResponse<List<PostCardItemResponse>> getPostCardItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Posts> pagePosts = postRepository.findAll(pageable);
        List<Posts> listPost = pagePosts.getContent();
        var pagination = Pagination.builder()
                .page(page)
                .limit(size)
                .totalPages(pagePosts.getTotalPages())
                .totalRecords(pagePosts.getTotalElements())
                .build();
        List<PostCardItemResponse> listPostCardItemResponse =
                listPost.stream().map(postMapper::toPostCardItemResponse).toList();
        return ApiResponse.<List<PostCardItemResponse>>builder()
                .message("Success")
                .data(listPostCardItemResponse)
                .pagination(pagination)
                .build();
    }
}
