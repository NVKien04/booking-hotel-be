package com.example.booking_hotel.repository;

import com.example.booking_hotel.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    boolean existsByRefreshToken(String token);
}
