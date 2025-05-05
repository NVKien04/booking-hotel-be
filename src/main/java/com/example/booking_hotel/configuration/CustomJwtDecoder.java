package com.example.booking_hotel.configuration;

import com.example.booking_hotel.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomJwtDecoder {
    @Value("${jwt.signerKey}")
    private String signerKey;

    @Autowired
    AuthService authService;


}
