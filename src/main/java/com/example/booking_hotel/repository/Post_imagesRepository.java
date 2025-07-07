package com.example.booking_hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booking_hotel.entity.Post_images;

@Repository
public interface Post_imagesRepository extends JpaRepository<Post_images, String> {}
