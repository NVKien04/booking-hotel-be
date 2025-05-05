package com.example.booking_hotel.service;

import com.example.booking_hotel.dto.request.PostCreateRequest;
import com.example.booking_hotel.dto.response.PostResponse;
import com.example.booking_hotel.entity.Posts;
import com.example.booking_hotel.entity.User;
import com.example.booking_hotel.enums.Accommodation_type;
import com.example.booking_hotel.mapper.PostMapper;
import com.example.booking_hotel.repository.AmenitieRepository;
import com.example.booking_hotel.repository.PostRepository;
import com.example.booking_hotel.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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


    UploadService uploadService;

    PostMapper postMapper;

    public PostResponse create(PostCreateRequest request){
        Posts posts = postMapper.toPosts(request);
        User owner = userRepository.findById(request.getOwner())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getOwner()));
        posts.setAccommodation_type(Accommodation_type.HOTEL);
        posts.setThumbnail(uploadService.uploadFile(request.getThumbnail(), thumbnailPath));
        posts.setOwner(owner);
        log.info("Post created");
        return postMapper.toPostResponse(postRepository.save(posts));
    }

    public List<PostResponse> getAll(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        List<Posts> listPosts = postRepository.findAll(pageable).getContent();
         return listPosts.stream().map(postMapper::toPostResponse).collect(Collectors.toList());
    }

}
