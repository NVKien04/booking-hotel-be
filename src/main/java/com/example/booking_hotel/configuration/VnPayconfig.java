package com.example.booking_hotel.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "vnpay")
@Getter
@Setter
public class VnPayconfig {

    private String tmnCode;
    private String hashSecret;
    private String payUrl;
    private String returnUrl;
    private String version;
    private String command;
    private String currency;
    private String locale;
}
