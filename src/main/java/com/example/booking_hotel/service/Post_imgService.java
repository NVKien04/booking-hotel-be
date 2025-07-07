package com.example.booking_hotel.service;

import org.springframework.web.multipart.MultipartFile;

public interface Post_imgService {

    public void uploadMultipleImg_Post(MultipartFile[] files, String post_id);
}
