package com.example.booking_hotel.entity;

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
public class Place_type extends BaseEnumEntity {

    String name;
    String icon;
    String description;

    @OneToMany(mappedBy = "placeType")
    List<Posts> posts = new ArrayList<>();
}
