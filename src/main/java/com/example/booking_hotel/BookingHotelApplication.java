package com.example.booking_hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.booking_hotel.repository")
public class BookingHotelApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookingHotelApplication.class, args);
    }
}
