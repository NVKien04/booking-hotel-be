package com.example.booking_hotel.controller.Post_img.Post;

import org.springframework.web.bind.annotation.*;

import com.example.booking_hotel.service.Post_imgService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/post_img")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Post_ImgController {
    Post_imgService postImgService;

    //     @PostMapping("/upload_post_img")
    //    public ApiResponse<List<Post_ImgResponse>>  uploadPostImg(@RequestParam String postId,@RequestParam("files")
    // MultipartFile[] files) throws IOException {
    //
    //         return postImgService.uploadMultipleImg_Post(files, postId);
    //     }

}
