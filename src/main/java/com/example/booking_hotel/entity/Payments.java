package com.example.booking_hotel.entity;

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

    @OneToOne
    @JoinColumn(name = "booking_id", unique = true)
    Bookings booking;

    @ManyToOne
            @JoinColumn(name = "status_id")
    PaymentStatus status;
    String method;
    BigDecimal amount;
}
