package com.example.booking_hotel.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ExChangeTokenRequest {
    @NotBlank(message = "Code không được để trống")
    String code;

    @NotBlank(message = "clientId không được để trống")
    String clientId;

    @NotBlank(message = "clientSecret không được để trống")
    String clientSecret;

    @NotBlank(message = "redirectUri không được để trống")
    String redirectUri;

    @NotBlank(message = "grantType không được để trống")
    String grantType;
}
