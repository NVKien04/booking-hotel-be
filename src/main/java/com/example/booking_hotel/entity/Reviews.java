package com.example.booking_hotel.entity;

import com.example.booking_hotel.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Reviews extends BaseEntity {



    int rating;
    @Column(columnDefinition = "TEXT")
    String comment;

    @OneToOne(mappedBy = "reviews")
    Bookings bookings;

   @ManyToOne
   @JoinColumn(name = "user", nullable = false)
    User user;



}
