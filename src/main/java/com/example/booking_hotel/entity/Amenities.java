package com.example.booking_hotel.entity;

import com.example.booking_hotel.enums.Amenity;
import com.example.booking_hotel.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Amenities extends BaseEntity {


    @ManyToMany(mappedBy = "amenities")
    private Set<Posts> posts = new HashSet<>();
    @Enumerated(EnumType.STRING)
    Amenity amenity;




}
