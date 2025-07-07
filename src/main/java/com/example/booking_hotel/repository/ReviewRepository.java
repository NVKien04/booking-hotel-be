package com.example.booking_hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booking_hotel.entity.Reviews;

@Repository
public interface ReviewRepository extends JpaRepository<Reviews, String> {}
