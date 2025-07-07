package com.example.booking_hotel.mapper;

import org.mapstruct.Mapper;

import com.example.booking_hotel.dto.response.Place_typeResponse.Place_typeResponse;
import com.example.booking_hotel.entity.Place_type;

@Mapper
public interface Place_typeMapper {
    Place_typeResponse toPlaceTypeResponse(Place_type entity);
}
