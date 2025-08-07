package com.example.booking_hotel.repository;

import com.example.booking_hotel.entity.RedisRevokedToken;
import com.example.booking_hotel.entity.RedisVerificationCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevokedTokenCodeRepository extends CrudRepository<RedisRevokedToken, String> {


    boolean existsById(String accessToken);
}
