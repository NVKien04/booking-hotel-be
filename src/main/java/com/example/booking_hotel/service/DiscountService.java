package com.example.booking_hotel.service;

import com.example.booking_hotel.dto.request.post.PostCreateRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.post.PostCardItemResponse;
import com.example.booking_hotel.dto.response.post.PostDetailResponse;
import com.example.booking_hotel.dto.response.post.PostResponse;


import java.time.LocalDate;
import java.util.List;

public interface PostService {

    public PostResponse create(PostCreateRequest request);

    public ApiResponse<List<PostCardItemResponse>> search(int page, int size, String sort, String search);

    public ApiResponse<PostDetailResponse>  getPostDetail(String id);

    public List<LocalDate> getSelectDates(String id);

    public ApiResponse<List<PostCardItemResponse>> getPostCardItems(int page, int size);



}
