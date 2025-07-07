package com.example.booking_hotel.entity;

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
public class Reviews extends BaseEntity {

    int rating;

    @Column(columnDefinition = "TEXT")
    String comment;

    @ManyToOne
    @JoinColumn(name = "boonking_id")
    Bookings booking;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    Posts post;
}
