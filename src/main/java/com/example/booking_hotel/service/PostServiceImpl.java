package com.example.booking_hotel.service;

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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {

    @NonFinal
            @Value("${file.upload-post}")
            String thumbnailPath;

    PostRepository postRepository;

    UserRepository userRepository;

    AmenitiesRepository amenitiesRepository;

    Place_TypeRepository   placeTypeRepository;

    Post_imagesRepository post_imagesRepository;


    UploadService uploadService;

    PostMapper postMapper;

    public PostResponse create(PostCreateRequest request){
        Posts posts = postMapper.toPosts(request);
        User owner = userRepository.findById(request.getOwner())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getOwner()));
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

    public ApiResponse<List<PostCardItemResponse>> getAll(int page, int size, String sort, String search){
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

    public ApiResponse<PostDetailResponse>  getPostDetail(String id){
            Posts post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));
            var kien = postMapper.toPostDetailResponse(post);
            return ApiResponse.<PostDetailResponse>builder()
                .code(1)
                .message("success")
                .data(postMapper.toPostDetailResponse(post))
                .build();
    }



}
