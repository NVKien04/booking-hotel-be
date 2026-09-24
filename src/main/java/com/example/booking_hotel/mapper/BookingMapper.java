package com.example.booking_hotel.mapper;

import org.mapstruct.Mapper;

import com.example.booking_hotel.dto.request.BookingCreateRequest;
import com.example.booking_hotel.dto.response.BookingResponse;
import com.example.booking_hotel.entity.Bookings;

@Mapper(
        componentModel = "spring",
        uses = {PostMapper.class, ReviewsMapper.class})
public interface BookingMapper {

    Bookings toBooking(BookingCreateRequest bookingCreateRequest);

    BookingResponse toBookingresponse(Bookings bookings);
}
