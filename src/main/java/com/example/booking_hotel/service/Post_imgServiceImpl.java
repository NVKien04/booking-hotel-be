package com.example.booking_hotel.service;

import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.post_img.Post_ImgResponse;
import com.example.booking_hotel.entity.Post_images;
import com.example.booking_hotel.entity.Posts;
import com.example.booking_hotel.mapper.Post_ImgMapper;
import com.example.booking_hotel.repository.PostRepository;
import com.example.booking_hotel.repository.Post_imagesRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Post_imgService {
    Post_imagesRepository post_imagesRepository;
    UploadService uploadService;
    PostRepository postRepository;
    Post_ImgMapper postImgMapper;


    @Transactional
    public ApiResponse<List<Post_ImgResponse>>   uploadMultipleImg_Post(MultipartFile[] files, String post_id){

        List<Post_images> listImg_post = new ArrayList<>();

        Posts post = postRepository.findById(post_id).
                orElseThrow(() -> new RuntimeException("Post not found with ID: " + post_id));
        for(MultipartFile file : files){
            if(!file.isEmpty()){
                if(!file.getContentType().startsWith("image/")){
                    return ApiResponse.<List<Post_ImgResponse>>builder()
                            .message("Tất cả file phải là ảnh")
                            .code(0)
                            .build();
                }
                try{
                    String url_img = uploadService.uploadFile(file, "posts", "post");
                    Post_images post_images = new Post_images();
                    post_images.setPosts(post);
                    post_images.setImage_url(url_img);
                    listImg_post.add(post_images);
                }
                catch(Exception e){
                    return ApiResponse.<List<Post_ImgResponse>>builder()
                            .code(0)
                            .message("Lỗi lưu file: "+file.getOriginalFilename())
                            .build();
                }

            }
        }
        List<Post_ImgResponse> Post_Img_Response =  post_imagesRepository.saveAll(listImg_post).stream().map(postImgMapper::toPost_ImgResponse).toList();
        return ApiResponse.<List<Post_ImgResponse>>builder()
                .code(1)
                .message("Success")
                .data(Post_Img_Response)
                .build();

    }
}
