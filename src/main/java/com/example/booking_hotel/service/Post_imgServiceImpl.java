package com.example.booking_hotel.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.booking_hotel.entity.Post_images;
import com.example.booking_hotel.entity.Posts;
import com.example.booking_hotel.exception.AppException;
import com.example.booking_hotel.exception.ErrorCode;
import com.example.booking_hotel.mapper.Post_ImgMapper;
import com.example.booking_hotel.repository.PostRepository;
import com.example.booking_hotel.repository.Post_imagesRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Post_imgServiceImpl implements Post_imgService {
    Post_imagesRepository post_imagesRepository;
    UploadService uploadService;
    PostRepository postRepository;
    Post_ImgMapper postImgMapper;

    @Override
    @Transactional
    public void uploadMultipleImg_Post(MultipartFile[] files, String post_id) {

        List<Post_images> listImg_post = new ArrayList<>();

        Posts post = postRepository.findById(post_id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                if (!file.getContentType().startsWith("image/")) {
                    throw new AppException(ErrorCode.INVALID_IMG);
                }
                try {
                    String url_img = uploadService.uploadFile(file, "posts", "post");
                    Post_images post_images = new Post_images();
                    post_images.setPosts(post);
                    post_images.setImage_url(url_img);
                    listImg_post.add(post_images);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        post_imagesRepository.saveAll(listImg_post);
    }
}
