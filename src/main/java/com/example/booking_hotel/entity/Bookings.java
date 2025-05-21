package com.example.booking_hotel.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Reviews> reviews = new ArrayList<>();
    Date check_in;
    Date check_out;
    BigDecimal totalPrice;
    int numberOfAdults;
    int numberOfChildren;
    @ManyToOne
    @JoinColumn(name = "status_id")
    BookingStatus status;






}
