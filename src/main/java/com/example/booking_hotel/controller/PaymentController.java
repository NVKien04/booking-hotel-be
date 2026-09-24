package com.example.booking_hotel.controller;

import java.util.Collections;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.service.Impl.VnPayService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/payment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

    VnPayService vnPayService;

    @GetMapping("/create")
    public ResponseEntity<ApiResponse<Map<String, String>>> createPayment(
            HttpServletRequest request, @RequestParam("amount") Long amount) {
        try {
            String paymentUrl = vnPayService.createPaymentUrl(request, amount);
            return ResponseEntity.ok(
                    ApiResponse.success("Tạo URL thanh toán thành công", Collections.singletonMap("url", paymentUrl)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi tạo URL thanh toán"));
        }
    }
}
