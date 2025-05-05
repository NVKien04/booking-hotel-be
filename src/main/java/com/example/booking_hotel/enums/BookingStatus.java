package com.example.booking_hotel.enums;

public enum BookingStatus {
    PENDING("PENDING"),
    CONFIRMED("CONFIRMED"),
    CANCELLED("CANCELLED"),
    CHECKED_IN("CHECKED_IN"),
    CHECKED_OUT("CHECKED_OUT"),
    COMPLETED("COMPLETED"),
    NO_SHOW("NO_SHOW"),
    REFUNDED("REFUNDED"),;

    private String name;
    BookingStatus(String name) {
        this.name = name;
    }
}
