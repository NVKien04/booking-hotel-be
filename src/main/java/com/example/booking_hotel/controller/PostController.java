package com.example.booking_hotel.controller;

import java.util.List;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.booking_hotel.dto.request.PostCreateRequest;
import com.example.booking_hotel.dto.request.PostSearchRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.PostCardItemResponse;
import com.example.booking_hotel.dto.response.PostDetailResponse;
import com.example.booking_hotel.dto.response.PostResponse;
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
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @Valid @ModelAttribute PostCreateRequest postCreateRequest) {
        PostResponse postResponse = postService.create(postCreateRequest);
        return ResponseEntity.ok(ApiResponse.success(postResponse));
    }

    @GetMapping("/fetchPost")
    public ResponseEntity<ApiResponse<List<PostCardItemResponse>>> getPost(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @ModelAttribute PostSearchRequest postSearchRequest,
            @RequestParam(defaultValue = "rating,asc") String sort) {
        var response = postService.search(page, size, sort, postSearchRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/home")
    public ResponseEntity<ApiResponse<List<PostCardItemResponse>>> getHome(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
        var response = postService.getPostCardItems(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> getPostDetail(@PathVariable String id) {
        var response = postService.getPostDetail(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable String id) {
        postService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.success("delete Success"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteMultiplePost(
            @RequestBody @NotEmpty(message = "Không được để null!") Set<String> ids) {
        postService.deleteMultiplePosts(ids);
        return ResponseEntity.ok(ApiResponse.success("delete multiple Success"));
    }
}
