package com.example.booking_hotel.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.booking_hotel.entity.RedisRevokedToken;

@Repository
public interface RevokedTokenCodeRepository extends CrudRepository<RedisRevokedToken, String> {

    boolean existsById(String accessToken);
}
