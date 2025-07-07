package com.example.booking_hotel.enums;

public enum Booking_status {
    CONFIRMED("Đã xác nhận"),
    CHECKED_IN("Đã nhận phòng"),
    CHECKED_OUT("Đã trả phòng"),
    CANCELLED("Đã huỷ"),
    NO_SHOW("Vắng mặt không báo"),
    COMPLETED("Hoàn tất");

    private final String displayName;

    Booking_status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

