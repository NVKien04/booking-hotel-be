package com.example.booking_hotel.enums;

public enum Accommodation_type {
    HOTEL("Hotel"),
    HOMESTAY("Homestay"),
    APARTMENT("Apartment"),
    ;

    private String name;

    Accommodation_type(String name) {
        this.name = name;
    }
}
