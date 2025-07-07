package com.example.booking_hotel.repository;

import com.example.booking_hotel.entity.Amenities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmenitiesRepository extends JpaRepository<Amenities, String> {

}
