package com.example.booking_hotel.enums;

import lombok.Getter;

@Getter
public enum Booking_status {
    CONFIRMED("CONFIRMED", "Đã xác nhận"),
    CHECKED_IN("CHECKED_IN", "Đã nhận phòng"),
    CHECKED_OUT("CHECKED_OUT", "Đã trả phòng"),
    CANCELLED("CANCELLED", "Đã huỷ"),
    DRAFT("DRAFT ", "Người dùng mới bắt đầu quá trình đặt, chưa hoàn tất "),
    COMPLETED("COMPLETED", "Hoàn tất"),
    PAYMENT_PROCESSING("PAYMENT_PROCESSING", "Đang thanh toán"),
    PAYMENT_FAILED("PAYMENT_FAILED", "Thanh toán thất bại");

    private final String code;
    private final String displayName;

    Booking_status(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
}
