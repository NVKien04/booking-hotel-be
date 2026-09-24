package com.example.booking_hotel.dto.request;

import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogoutTokenRequest {

    @NotBlank(message = "accessToken không được để trống")
    String accessToken;

    @NotBlank(message = "refreshToken không được để trống")
    String refreshToken;
}
