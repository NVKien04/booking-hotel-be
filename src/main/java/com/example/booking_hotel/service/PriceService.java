package com.example.booking_hotel.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.booking_hotel.dto.PriceDto;
import com.example.booking_hotel.entity.PostsAvailability;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PriceService {
    DiscountService discountService;

    public PriceDto calculatePrice(List<PostsAvailability> aDays) {

        var night = aDays.size();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (var aDay : aDays) {
            subtotal = subtotal.add(aDay.getPrice());
        }
        var discount = discountService.calculateDiscount(night, subtotal);

        return PriceDto.builder()
                .subtotal(subtotal)
                .discount(discount)
                .totalAmount(subtotal.add(discount))
                .build();
    }
}
