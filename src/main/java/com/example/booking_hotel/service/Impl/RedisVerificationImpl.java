package com.example.booking_hotel.service.Impl;

import com.example.booking_hotel.entity.RedisVerificationCode;
import com.example.booking_hotel.enums.VerificationType;
import com.example.booking_hotel.exception.AppException;
import com.example.booking_hotel.exception.ErrorCode;
import com.example.booking_hotel.repository.VerificationCodeRepository;
import com.example.booking_hotel.service.RedisVerificationCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j(topic = "VERIFICATION-CODE-SERVICE")
@RequiredArgsConstructor
public class RedisVerificationImpl implements RedisVerificationCodeService{

    VerificationCodeRepository verificationCodeRepository;
    @Override
    public RedisVerificationCode saveVerificationCode(String email, VerificationType type) {
        String verificationCode = generateVerificationCode();

        LocalDateTime expirationTime = getExpirationTimeByType(type);

        long tlt = java.time.Duration.between(LocalDateTime.now(), expirationTime).getSeconds();

        String redisKey = email + ":" + verificationCode;

        RedisVerificationCode redisVerificationCode = RedisVerificationCode.builder()
                .redisKey(redisKey)
                .email(email)
                .verificationCode(verificationCode)
                .expirationTime(expirationTime)
                .ttl(tlt)
                .build();
        return verificationCodeRepository.save(redisVerificationCode);

    }
    @Override
    public RedisVerificationCode findVerificationCode(String email, VerificationType type, String verificationCode) {

        String redisKey = email + ":" + verificationCode;

        RedisVerificationCode redisVerificationCode = verificationCodeRepository.findById(redisKey).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));

        if(redisVerificationCode.getExpirationTime().isBefore(LocalDateTime.now())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        if(redisVerificationCode.getVerificationCode().equals(verificationCode)){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return redisVerificationCode;
    }

    private LocalDateTime getExpirationTimeByType(VerificationType type){

        switch (type){
            case FORGOT_PASSWORD -> {
               return  LocalDateTime.now().plusMinutes(180);
            }
            case RECOVER_ACCOUNT -> {
                return  LocalDateTime.now().plusMinutes(300);
            }
            default -> throw new AppException(ErrorCode.ROOM_ALREADY_BOOKED);
        }
    }


    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(1_000_000);

        return  String.format("%06d", code);
    }
}
