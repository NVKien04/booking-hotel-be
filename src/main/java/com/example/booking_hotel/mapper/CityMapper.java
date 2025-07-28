package com.example.booking_hotel.mapper;

import com.example.booking_hotel.dto.response.CityResponse;
import com.example.booking_hotel.dto.response.amenities.AmenitiesResponse;
import com.example.booking_hotel.entity.Amenities;
import com.example.booking_hotel.entity.City;
import org.mapstruct.Mapper;
//@Mapper(
//        componentModel = "spring",
//        uses ={DistrictMapper.class})
@Mapper
public interface CityMapper {
    CityResponse toCityResponse(City city);
}
