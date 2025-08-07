package com.example.booking_hotel.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "districts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class District {

    @Id
    @GeneratedValue
    String id;

    @Column(nullable = false)
    String name; // VD: Quận 1, Cầu Giấy

    @Column(nullable = false, unique = true)
    String slug; // VD: ho-chi-minh

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    City city;

    // Optional: nếu District có danh sách Post
    // @OneToMany(mappedBy = "district")
    // List<Post> posts;
}
