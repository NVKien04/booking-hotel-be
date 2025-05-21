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

public class Place_type extends BaseEnumEntity {

    String name;
    String icon;
    String description;

    @OneToMany(mappedBy = "place_type")
    List<Posts> posts = new ArrayList<>();
}
