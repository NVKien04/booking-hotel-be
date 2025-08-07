package com.example.booking_hotel.mapper;

import org.mapstruct.Mapper;

import com.example.booking_hotel.entity.District;

@Mapper
public interface DistrictMapper {
    District toDistrictResponse(District district);
}
