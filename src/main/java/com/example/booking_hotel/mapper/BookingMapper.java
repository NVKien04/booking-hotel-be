package com.example.booking_hotel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.booking_hotel.dto.request.BookingCreateRequest;
import com.example.booking_hotel.dto.response.BookingResponse;
import com.example.booking_hotel.entity.Bookings;

@Mapper(
        componentModel = "spring",
        uses = {PostMapper.class, ReviewsMapper.class})
public interface BookingMapper {

    Bookings toBooking(BookingCreateRequest bookingCreateRequest);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "stats", target = "status")
    @Mapping(source = "checkIn", target = "check_in")
    @Mapping(source = "checkOut", target = "check_out")
    @Mapping(source = "posts.id", target = "postId")
    @Mapping(source = "posts.title", target = "postTitle")
    @Mapping(source = "posts.thumbnail", target = "postThumbnail")
    BookingResponse toBookingresponse(Bookings bookings);
}
