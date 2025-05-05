package com.example.booking_hotel.enums;

public enum PaymentStatus {
    PENDING("PENDING"),
    PAID("PAID"),
    FAILED("FAILED"),
    REFUNDED("REFUNDED"),
    CANCELLED("CANCELLED");

    private String name;
    PaymentStatus(String name) {
        this.name = name;
    }
}
