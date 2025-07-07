package com.example.booking_hotel.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class DiscountService {

    private static final int LONG_STAY = 3;
    private static final BigDecimal LONG_STAY_DISCOUNT_RATE = BigDecimal.valueOf(-0.05);

    /**
     * Tính giảm giá dựa trên số đêm và tổng phụ (subtotal).
     * @param nights Số đêm ở.
     * @param subtotal Tổng giá chưa giảm.
     * @return Số tiền giảm (âm nếu giảm).
     */
    public BigDecimal calculateDiscount(long nights, BigDecimal subtotal) {
        if (nights >= LONG_STAY) {
            return subtotal.multiply(LONG_STAY_DISCOUNT_RATE); // âm 5%
        }
        return BigDecimal.ZERO;
    }
}
