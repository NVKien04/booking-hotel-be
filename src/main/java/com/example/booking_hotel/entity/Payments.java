package com.example.booking_hotel.entity;

import com.example.booking_hotel.enums.PaymentStatus;
import com.example.booking_hotel.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Payments extends BaseEntity {


    @OneToOne(mappedBy = "payments")
    Bookings bookings;


    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;

    String method;
    BigDecimal amount;
}
