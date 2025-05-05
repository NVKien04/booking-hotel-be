package com.example.booking_hotel.controller.Post;

import com.example.booking_hotel.dto.request.PostCreateRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.PostResponse;
import com.example.booking_hotel.service.PostService;
import com.example.booking_hotel.service.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/post")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @PostMapping("/create")
    private ApiResponse<PostResponse> createPost(@ModelAttribute PostCreateRequest postCreateRequest) {
        PostResponse postResponse = postService.create(postCreateRequest);
        log.info("Post created");

        return ApiResponse.<PostResponse >builder()
                .data(postResponse)
                .message("Success")
                .build();
    }

    @GetMapping("/getAllPost")
    private ApiResponse<List<PostResponse>> getAllPost() {
        List<PostResponse> listPosts = postService.getAll(0,2);
        return ApiResponse.<List<PostResponse>>builder()
                .message("success")
                .data(listPosts)
                .build();
    }


    @GetMapping("/thumbnail_post/{thumbnail}")
    private ResponseEntity<Resource> thumbnail(@PathVariable String thumbnail) {
        try{
            Path filePath = Paths.get("uploads/thumbnail_post").resolve(thumbnail).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            String contentType = Files.probeContentType(filePath);


            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        }
        catch (MalformedURLException e){
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





}
