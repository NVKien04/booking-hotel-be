package com.example.booking_hotel.entity;

import java.util.HashSet;
import java.util.Set;

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
public class Amenities extends BaseEnumEntity {

    @ManyToMany(mappedBy = "amenities")
    private Set<Posts> posts = new HashSet<>();

    String name;
    String icon;
}
