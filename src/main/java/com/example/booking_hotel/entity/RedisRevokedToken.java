package com.example.booking_hotel.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("RedisRevokedToken")
public class RedisRevokedToken {
    @Id
    String accessToken;

    String email;
    Date expiryTime;

    @TimeToLive
    long ttl;
}
