package com.example.booking_hotel.repository;

import com.example.booking_hotel.entity.Payments;
import com.example.booking_hotel.entity.Place_type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymetRepository extends JpaRepository<Payments, String> {

    @Override
    Optional<Payments> findById(String s);
}
