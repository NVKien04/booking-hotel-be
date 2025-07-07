package com.example.booking_hotel.controller.Post;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.booking_hotel.dto.request.post.PostCreateRequest;
import com.example.booking_hotel.dto.request.post.PostSearchRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.post.PostCardItemResponse;
import com.example.booking_hotel.dto.response.post.PostDetailResponse;
import com.example.booking_hotel.dto.response.post.PostResponse;
import com.example.booking_hotel.service.PostService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/post")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;
    @PreAuthorize("hasRole('RENTER')")
    @PostMapping("/create")
    public ApiResponse<PostResponse> createPost(@ModelAttribute PostCreateRequest postCreateRequest) {
        PostResponse postResponse = postService.create(postCreateRequest);
        return ApiResponse.<PostResponse>builder()
                .data(postResponse)
                .message("Success")
                .build();
    }
    @GetMapping("/search")
    public ApiResponse<List<PostCardItemResponse>> getPost(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @ModelAttribute PostSearchRequest postSearchRequest,
            @RequestParam(defaultValue = "rating,asc") String sort) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication : {}", authentication.getName());
        authentication
                .getAuthorities()
                .forEach(grantedAuthority -> log.info("GrantedAuthority : {}", grantedAuthority.getAuthority()));
        return postService.search(page, size, sort, postSearchRequest);
    }
    @GetMapping("/home")
    public ApiResponse<List<PostCardItemResponse>> getHome(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return postService.getPostCardItems(page, size);
    }
    @GetMapping("/{id}")
    public ApiResponse<PostDetailResponse> getPostDetail(@PathVariable String id) {
        return postService.getPostDetail(id);
    }
}
    