package com.example.booking_hotel.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.booking_hotel.entity.RedisVerificationCode;

public interface VerificationCodeRepository extends CrudRepository<RedisVerificationCode, String> {}
