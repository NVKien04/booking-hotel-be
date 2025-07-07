package com.example.booking_hotel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least 8 characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least 16+", HttpStatus.BAD_REQUEST),
    POST_NOT_EXISTED(1009, "Post not existed", HttpStatus.NOT_FOUND),
    EMAIL_EXISTED(1010, "Email existed", HttpStatus.NOT_FOUND),
    BAD_CREDENTIALS(1011, "Email hoặc mật khẩu không hợp lệ ", HttpStatus.UNAUTHORIZED),
    CANNOT_BOOK_OWN_ROOM(1012, "Không thể đặt phòng của chính bạn.", HttpStatus.FORBIDDEN),
    ROOM_NOT_FOUND(1013, "Phòng không tồn tại.", HttpStatus.NOT_FOUND),
    INVALID_DATES(1014, "Ngày đặt phòng không hợp lệ.", HttpStatus.BAD_REQUEST),
    INVALID_GUEST(1016, "Số lượng khác không hợp lệ.", HttpStatus.BAD_REQUEST),
    INVALID_IMG(1017, "File ảnh không hợp lệ.", HttpStatus.BAD_REQUEST),
    ROOM_ALREADY_BOOKED(1015, "Phòng đã được đặt trong thời gian này.", HttpStatus.CONFLICT),
    ;

    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
