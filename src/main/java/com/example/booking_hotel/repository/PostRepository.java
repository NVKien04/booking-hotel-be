package com.example.booking_hotel.repository;

import com.example.booking_hotel.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Posts, String> {
    Page<Posts> findAll(Pageable pageable);
}
