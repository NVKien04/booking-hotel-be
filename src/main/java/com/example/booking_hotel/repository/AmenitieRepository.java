package com.example.booking_hotel.repository;

import com.example.booking_hotel.entity.Amenities;
import com.example.booking_hotel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AmenitieRepository extends JpaRepository<Amenities, String> {

}
