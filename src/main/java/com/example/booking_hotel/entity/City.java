package com.example.booking_hotel.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class City {

    @Id
    @GeneratedValue
    String id;

    @Column(nullable = false)
    String name; // VD: TP. Hồ Chí Minh

    @Column(nullable = false, unique = true)
    String slug; // VD: ho-chi-minh

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    List<District> districts;
}