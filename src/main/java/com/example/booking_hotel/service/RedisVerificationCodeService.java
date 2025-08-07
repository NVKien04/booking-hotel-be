package com.example.booking_hotel.service;

import com.example.booking_hotel.entity.RedisVerificationCode;
import com.example.booking_hotel.enums.VerificationType;

public interface RedisVerificationCodeService {
    RedisVerificationCode saveVerificationCode(String email, VerificationType type);

    RedisVerificationCode findVerificationCode(String email, VerificationType type, String verificationCode);
}
