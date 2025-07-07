package com.example.booking_hotel.enums;

public enum Payment_status {
    UNPAID("UNPAID", "Chưa thanh toán"),
    PENDING("PENDING", "Đang xử lý"),
    PAID("PAID", "Đã thanh toán"),
    FAILED("FAILED", "Thanh toán thất bại"),
    REFUNDED("REFUNDED", "Đã hoàn tiền");

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