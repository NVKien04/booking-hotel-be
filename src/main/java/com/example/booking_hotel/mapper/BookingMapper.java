package com.example.booking_hotel.mapper;

import org.mapstruct.Mapper;

import com.example.booking_hotel.dto.request.booking.BookingCreateRequest;
import com.example.booking_hotel.dto.response.booking.BookingResponse;
import com.example.booking_hotel.entity.Bookings;

@Mapper(
        componentModel = "spring",
        uses = {PostMapper.class, ReviewsMapper.class})
public interface BookingMapper {

    Bookings toBooking(BookingCreateRequest bookingCreateRequest);

    BookingResponse toBookingresponse(Bookings bookings);
}
