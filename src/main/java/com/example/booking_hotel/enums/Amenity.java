package com.example.booking_hotel.enums;

public enum Amenity {
    RENTER("RENTER"),
    HOST("HOST");

    private String name;
    Amenity(String name) {
        this.name = name;
    }
}
