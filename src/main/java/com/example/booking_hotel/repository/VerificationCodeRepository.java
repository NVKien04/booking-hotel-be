package com.example.booking_hotel.repository;

import com.example.booking_hotel.entity.RedisVerificationCode;
import org.springframework.data.repository.CrudRepository;

public interface VerificationCodeRepository extends CrudRepository<RedisVerificationCode, String> {
}
