package com.example.booking_hotel.mapper;

import org.mapstruct.Mapper;

import com.example.booking_hotel.dto.response.AmenitiesResponse;
import com.example.booking_hotel.entity.Amenities;

@Mapper
public interface AmenitiesMapper {
    AmenitiesResponse toAmenitiesResponse(Amenities amenities);
}
