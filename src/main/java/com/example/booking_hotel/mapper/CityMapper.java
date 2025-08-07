package com.example.booking_hotel.mapper;

import org.mapstruct.Mapper;

import com.example.booking_hotel.dto.response.CityResponse;
import com.example.booking_hotel.entity.City;

@Mapper
public interface CityMapper {
    CityResponse toCityResponse(City city);
}
