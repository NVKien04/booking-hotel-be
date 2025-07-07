package com.example.booking_hotel.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
public class Bookings extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    Posts posts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Reviews> reviews = new ArrayList<>();

    @Column(name = "check_in")
    @Temporal(TemporalType.DATE)
    LocalDate checkIn;

    @Column(name = "check_out")
    @Temporal(TemporalType.DATE)
    LocalDate checkOut;

    BigDecimal totalPrice;
    BigDecimal discount;
    BigDecimal totalAmount;
    int guest;
    String stats;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<PostsAvailability> postsAvailabilities = new ArrayList<>();
}
