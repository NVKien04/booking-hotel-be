package com.example.booking_hotel.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payments extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "booking_id", unique = true)
    Bookings booking;

    String status;
    String method;
    BigDecimal amount;
    String txn_ref;
    String response_code;
    String transaction_status;
    LocalDateTime transaction_date;
}
