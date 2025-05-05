package com.example.booking_hotel.entity;

import com.example.booking_hotel.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Bookings extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
            Posts posts;

    @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "user_id")
    User user;

    @OneToOne
            @JoinColumn(name = "payment_id", referencedColumnName = "id")
            Payments payments;

    @OneToOne
    @JoinColumn(name = "reviews_id", referencedColumnName = "id")
    Reviews reviews;

            

    Date check_in;
    Date check_out;
    BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    BookingStatus status;






}
