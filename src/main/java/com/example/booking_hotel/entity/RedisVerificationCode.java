package com.example.booking_hotel.entity;

import com.example.booking_hotel.enums.VerificationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("RedisVerificationCode")
public class RedisVerificationCode {

    @Id
    String redisKey;

    @Column(nullable = false)
    String email;
    @Column(nullable = false)
    String verificationCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    VerificationType verificationType;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime expirationTime;

    @TimeToLive
    long ttl;
}
