package com.example.booking_hotel.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.booking_hotel.entity.Bookings;

@Repository
public interface BookingRepository extends JpaRepository<Bookings, String> {

    @Query(
            "select b from Bookings b where b.posts.id = :postId and  b.stats = :Status And :newCheckIn < b.checkOut and :newCheckOut > b.checkIn")
    List<Bookings> findOverLappingBookings(
            @Param("postId") String postId,
            @Param("newCheckIn") LocalDate newCheckIn,
            @Param("newCheckOut") LocalDate newCheckout,
            @Param("Status") String Status);

    @Query("select b from Bookings b where b.posts.id = :postId and b.stats in :statusList")
    List<Bookings> findByPostIdAndStatusIn(
            @Param("postId") String postId, @Param("statusList") List<String> statusList);
}
