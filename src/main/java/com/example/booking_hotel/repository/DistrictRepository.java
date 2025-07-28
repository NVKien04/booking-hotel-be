package com.example.booking_hotel.repository;

import com.example.booking_hotel.entity.City;
import com.example.booking_hotel.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, String> {}
