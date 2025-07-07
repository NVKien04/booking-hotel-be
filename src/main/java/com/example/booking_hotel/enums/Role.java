package com.example.booking_hotel.enums;

public enum Role {
    RENTER("RENTER"),
    HOST("HOST");

    private String name;

    Role(String name) {
        this.name = name;
    }
}
