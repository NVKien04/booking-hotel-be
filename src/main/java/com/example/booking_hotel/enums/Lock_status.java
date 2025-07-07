package com.example.booking_hotel.enums;

import lombok.Getter;

@Getter
public enum Lock_status {
    UNLOCK("UNLOCK", "Mở khóa"),
    DRAFT("DRAFT", "Dữ phòng"),
    LOCK("LOCK", "Khóa");

    private final String code;
    private final String displayName;

    Lock_status(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

}
