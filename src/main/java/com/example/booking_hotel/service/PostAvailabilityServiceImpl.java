package com.example.booking_hotel.service;

import com.example.booking_hotel.configuration.SecurityUtil;
import com.example.booking_hotel.dto.request.post.PostCreateRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.Pagination;
import com.example.booking_hotel.dto.response.post.PostCardItemResponse;
import com.example.booking_hotel.dto.response.post.PostDetailResponse;
import com.example.booking_hotel.dto.response.post.PostResponse;
import com.example.booking_hotel.entity.Amenities;
import com.example.booking_hotel.entity.Place_type;
import com.example.booking_hotel.entity.Posts;
import com.example.booking_hotel.entity.User;
import com.example.booking_hotel.exception.AppException;
import com.example.booking_hotel.exception.ErrorCode;
import com.example.booking_hotel.mapper.PostMapper;
import com.example.booking_hotel.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    Place_TypeRepository   placeTypeRepository;


    SecurityUtil securityUtil;


    UploadService uploadService;

    PostMapper postMapper;

    @Override
    public PostResponse create(PostCreateRequest request){
        var userId = securityUtil.getCurrentUserId();
        Posts posts = postMapper.toPosts(request);
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new  AppException(ErrorCode.USER_NOT_EXISTED));
        posts.setThumbnail(uploadService.uploadFile(request.getThumbnail(), thumbnailPath, "post"));
        List<String> amenityIds = request.getAmenity_id();
        Set<Amenities> amenities = new HashSet<>(amenitiesRepository.findAllById(amenityIds));
        posts.setAmenities(amenities);
        Optional<Place_type> place_type = placeTypeRepository.findById(request.getPlace_type_id());
        posts.setPlace_type(place_type.orElse(null));
        posts.setOwner(owner);
        log.info("Post created");
        return postMapper.toPostResponse(postRepository.save(posts));
    }

    @Override
    public ApiResponse<List<PostCardItemResponse>> search(int page, int size, String sort, String search){
        Sort sortable = Sort.by("rating").ascending();
        if(sort != null && !sort.isEmpty()){
            String[] sortParams = sort.split(",");
            String sortField = sortParams[0];
            String sortDirection = sortParams.length > 1 ? sortParams[1] : "asc";
            sortable = sortDirection.equalsIgnoreCase("desc")
                    ? Sort.by(sortField).descending()
                    : Sort.by(sortField).ascending();

        }
        Pageable pageable = PageRequest.of(page, size, sortable);
        Page<Posts> pagePosts = (search == null || search.isEmpty())
                ? postRepository.findAll(pageable)
                : postRepository.findByTitleContainingIgnoreCase(search, pageable);

        List<Posts> listPost = pagePosts.getContent();

        var pagination = Pagination.builder()
                .page(page)
                .limit(size)
                .totalPages(pagePosts.getTotalPages())
                .totalRecords(pagePosts.getTotalElements())
                .build();
        List<PostCardItemResponse> listPostCardItemResponse = listPost.stream().map(postMapper::toPostCardItemResponse).toList();

         return ApiResponse.<List<PostCardItemResponse>>builder()
                 .message("Success")
                 .data(listPostCardItemResponse)
                 .pagination(pagination)
                 .build();
    }

    @Override
    public ApiResponse<PostDetailResponse>  getPostDetail(String id){
            Posts post = postRepository.findById(id).orElseThrow(() -> new  AppException(ErrorCode.POST_NOT_EXISTED));
            var postDetail = postMapper.toPostDetailResponse(post);
            postDetail.setAvailableDates(bookingService.getAvailableDate(id));

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


        Pageable  pageable = PageRequest.of(page, size);
        Page<Posts> pagePosts = postRepository.findAll(pageable);
        List<Posts> listPost = pagePosts.getContent();

        var pagination = Pagination.builder()
                .page(page)
                .limit(size)
                .totalPages(pagePosts.getTotalPages())
                .totalRecords(pagePosts.getTotalElements())
                .build();
        List<PostCardItemResponse> listPostCardItemResponse = listPost.stream().map(postMapper::toPostCardItemResponse).toList();

        return ApiResponse.<List<PostCardItemResponse>>builder()
                .message("Success")
                .data(listPostCardItemResponse)
                .pagination(pagination)
                .build();
    }




}
