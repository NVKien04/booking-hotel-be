package com.example.booking_hotel.controller.Post_img.Post;

import com.example.booking_hotel.dto.request.post.PostCreateRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.post.PostCardItemResponse;
import com.example.booking_hotel.dto.response.post.PostResponse;
import com.example.booking_hotel.dto.response.post.Post_ImgResponse;
import com.example.booking_hotel.service.PostService;
import com.example.booking_hotel.service.Post_imgService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/post_img")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Post_ImgController {
    Post_imgService postImgService;

     @PostMapping("/upload_post_img")
    public ApiResponse<List<Post_ImgResponse>>  uploadPostImg(@RequestParam String postId,@RequestParam("files") MultipartFile[] files) throws IOException {

         return postImgService.uploadMultipleImg_Post(files, postId);
     }

}
