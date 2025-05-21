package com.example.booking_hotel.controller.Post;

import com.example.booking_hotel.dto.request.post.PostCreateRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.post.PostCardItemResponse;
import com.example.booking_hotel.dto.response.post.PostResponse;
import com.example.booking_hotel.service.PostService;
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
    public ApiResponse<PostResponse> createPost(@ModelAttribute PostCreateRequest postCreateRequest) {
        PostResponse postResponse = postService.create(postCreateRequest);
        log.info("Post created");
        return ApiResponse.<PostResponse >builder()
                .data(postResponse)
                .message("Success")
                .build();
    }

    @GetMapping("/getAllPost")
    public ApiResponse<List<PostCardItemResponse>> getPost(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "rating,asc") String sort
    ) {
        return postService.getAll(page, size, sort, search);
    }


        @GetMapping("/thumbnail_post/{thumbnail}")
        public ResponseEntity<Resource> thumbnail(@PathVariable String thumbnail) {
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



    @GetMapping("/posts/{image_post}")
    public ResponseEntity<Resource> image_post(@PathVariable String image_post) {
        try{
            Path filePath = Paths.get("uploads/posts").resolve(image_post).normalize();
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
    @GetMapping("/{id}")
    public ApiResponse<PostResponse> getPostDetail(@PathVariable String id) {

        return ApiResponse.<PostResponse>builder().build();
    }





}
