package com.example.booking_hotel.service.Impl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booking_hotel.configuration.SecurityUtil;
import com.example.booking_hotel.dto.request.PostCreateRequest;
import com.example.booking_hotel.dto.request.PostSearchRequest;
import com.example.booking_hotel.dto.request.PostUpdateRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.Pagination;
import com.example.booking_hotel.dto.response.PostCardItemResponse;
import com.example.booking_hotel.dto.response.PostDetailResponse;
import com.example.booking_hotel.dto.response.PostResponse;
import com.example.booking_hotel.entity.*;
import com.example.booking_hotel.exception.AppException;
import com.example.booking_hotel.exception.ErrorCode;
import com.example.booking_hotel.mapper.PostMapper;
import com.example.booking_hotel.repository.*;
import com.example.booking_hotel.service.*;

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
        User owner = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Mapping request sang entity
        Posts post = postMapper.toPosts(request);
        post.setOwner(owner);

        City city = cityRepository
                .findById(request.getCityId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        District district = districtRepository
                .findById(request.getDistrictId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
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
        placeTypeRepository.findById(request.getPlaceType()).ifPresent(post::setPlaceType);

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
                search.getCity(),
                search.getGuest(),
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
    public void deletePost(String id) {
        Posts post = postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        postRepository.delete(post);
    }

    @Override
    public void deleteMultiplePosts(Set<String> ids) {
        Set<Posts> posts = new HashSet<>(postRepository.findAllById(ids));
        if (posts.isEmpty()) {
            throw new AppException(ErrorCode.POST_NOT_EXISTED);
        }
        postRepository.deleteAll(posts);
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

    @Override
    @Transactional
    public PostResponse update(String id, PostUpdateRequest request) {
        String userId = securityUtil.getCurrentUserId();
        Posts post = postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        if (!post.getOwner().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            post.setTitle(request.getTitle());
        }
        if (request.getShort_description() != null && !request.getShort_description().isBlank()) {
            post.setShort_description(request.getShort_description());
        }
        if (request.getNightPrice() != null) {
            post.setNightPrice(request.getNightPrice());
        }
        if (request.getWeekendPrice() != null) {
            post.setWeekendPrice(request.getWeekendPrice());
        }
        if (request.getCapacity() != null) {
            post.setCapacity(request.getCapacity());
        }
        if (request.getBedrooms() != null) {
            post.setBedrooms(request.getBedrooms());
        }
        if (request.getBathrooms() != null) {
            post.setBathrooms(request.getBathrooms());
        }
        if (request.getBeds() != null) {
            post.setBeds(request.getBeds());
        }
        if (request.getEffectivePetFriendly() != null) {
            post.setPet_friendly(request.getEffectivePetFriendly());
        }

        if (request.getEffectiveCityId() != null && !request.getEffectiveCityId().isBlank()) {
            cityRepository.findById(request.getEffectiveCityId()).ifPresent(post::setCity);
        }
        if (request.getEffectiveDistrictId() != null && !request.getEffectiveDistrictId().isBlank()) {
            districtRepository.findById(request.getEffectiveDistrictId()).ifPresent(post::setDistrict);
        }
        if (request.getAddressDetail() != null && !request.getAddressDetail().isBlank()) {
            post.setAddressDetail(request.getAddressDetail());
        }

        // Cập nhật fullAddress nếu có thay đổi về địa chỉ
        String addrDetail = post.getAddressDetail();
        String districtName = post.getDistrict() != null ? post.getDistrict().getName() : null;
        String cityName = post.getCity() != null ? post.getCity().getName() : null;
        String fullAddress = Stream.of(addrDetail, districtName, cityName)
                .filter(Objects::nonNull)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining(", "));
        post.setFullAddress(fullAddress);

        if (request.getThumbnail() != null && !request.getThumbnail().isEmpty()) {
            String thumbnailUrl = uploadService.uploadFile(request.getThumbnail(), thumbnailPath, "post");
            post.setThumbnail(thumbnailUrl);
        }

        if (request.getPlaceType() != null && !request.getPlaceType().isBlank()) {
            placeTypeRepository.findById(request.getPlaceType()).ifPresent(post::setPlaceType);
        }

        List<String> amenityIds = request.getEffectiveAmenityIds();
        if (amenityIds != null && !amenityIds.isEmpty()) {
            Set<Amenities> amenities = new HashSet<>(amenitiesRepository.findAllById(amenityIds));
            post.setAmenities(amenities);
        }

        Posts updatedPost = postRepository.save(post);

        if (request.getFiles() != null && request.getFiles().length > 0) {
            postImgService.uploadMultipleImg_Post(request.getFiles(), updatedPost.getId());
        }

        return postMapper.toPostResponse(updatedPost);
    }

    @Override
    public ApiResponse<List<PostCardItemResponse>> getMyPosts(int page, int size) {
        String userId = securityUtil.getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Posts> pagePosts = postRepository.findByOwnerId(userId, pageable);
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

