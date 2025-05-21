package com.example.booking_hotel.repository;

import com.example.booking_hotel.entity.Amenities;
import com.example.booking_hotel.entity.Place_type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Place_TypeRepository extends JpaRepository<Place_type, String> {

    @Override
    Optional<Place_type> findById(String s);
}
