package com.example.booking_hotel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booking_hotel.entity.Place_type;

@Repository
public interface Place_TypeRepository extends JpaRepository<Place_type, String> {

    @Override
    Optional<Place_type> findById(String s);
}
