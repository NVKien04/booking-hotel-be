package com.example.booking_hotel.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.example.booking_hotel.dto.request.post.PostCreateRequest;
import com.example.booking_hotel.dto.request.post.PostSearchRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.post.PostCardItemResponse;
import com.example.booking_hotel.dto.response.post.PostDetailResponse;
import com.example.booking_hotel.dto.response.post.PostResponse;

public interface PostService {

    public PostResponse create(PostCreateRequest request);

    public ApiResponse<List<PostCardItemResponse>> search(int page, int size, String sort, PostSearchRequest request);

    public ApiResponse<PostDetailResponse> getPostDetail(String id);

    public void deletePost(String ids);

    public void deleteMultiplePosts(Set<String> ids);

    public ApiResponse<List<PostCardItemResponse>> getPostCardItems(int page, int size);
}
