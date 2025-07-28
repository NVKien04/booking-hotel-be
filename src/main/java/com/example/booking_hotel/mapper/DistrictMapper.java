package com.example.booking_hotel.mapper;

import com.example.booking_hotel.dto.response.CityResponse;
import com.example.booking_hotel.entity.City;
import com.example.booking_hotel.entity.District;
import org.mapstruct.Mapper;

@Mapper
public interface DistrictMapper {
    District toDistrictResponse(District district);
}
