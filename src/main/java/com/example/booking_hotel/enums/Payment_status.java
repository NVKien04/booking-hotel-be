package com.example.booking_hotel.enums;

public enum Payment_status {
    INIT("INIT", "Chưa thanh toán"),
    SUCCESS("SUCCESS", "Thanh toán thành công"),
    FAILED("FAILED", "Thanh toán thất bại");

    private final String code;
    private final String displayName;

    Payment_status(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }
}
