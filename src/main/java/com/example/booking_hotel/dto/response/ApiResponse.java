package com.example.booking_hotel.dto.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    Date timestamp;

    @Builder.Default
    int code = 200;

    String message;
    T data;
    Pagination pagination;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .timestamp(new Date())
                .code(1000)
                .message("Success")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .timestamp(new Date())
                .code(1000)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .timestamp(new Date())
                .code(1000)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data, Pagination pagination) {
        return ApiResponse.<T>builder()
                .timestamp(new Date())
                .code(1000)
                .message(message)
                .data(data)
                .pagination(pagination)
                .build();
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.<T>builder()
                .timestamp(new Date())
                .code(code)
                .message(message)
                .build();
    }
}
