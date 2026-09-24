package com.example.booking_hotel.dto.response;

import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCodeResponse {

    String email;
    ;
    String verificationCode;
    LocalDateTime expirationTime;
}
