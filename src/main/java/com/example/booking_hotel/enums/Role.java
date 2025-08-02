package com.example.booking_hotel.enums;

import lombok.Getter;

@Getter
public enum Role {
    RENTER("RENTER"),
    HOST("HOST"),
    ADMIN("ADMIN");

    private String displayName;

    Role(String name) {
        this.displayName = displayName;
    }
}
