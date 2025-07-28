package com.example.booking_hotel.repository;

import com.example.booking_hotel.entity.Amenities;
import com.example.booking_hotel.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, String> {}
