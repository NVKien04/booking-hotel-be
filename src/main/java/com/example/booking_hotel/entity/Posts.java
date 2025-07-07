package com.example.booking_hotel.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
public class Posts extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    User owner;

    String title;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(columnDefinition = "TEXT")
    String short_description;

    @Column(name = "night_price", nullable = false)
    BigDecimal nightPrice;

    @Column(name = "weekend_price")
    BigDecimal weekendPrice;

    String thumbnail;
    int capacity;
    Boolean available;
    boolean pet_friendly;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Post_images> post_imagesList;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Bookings> bookings;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_amenities",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id"))
    Set<Amenities> amenities = new HashSet<>();
    int rating;
    int totalReviews;
    @PrePersist
    public void ensureActiveIsSet() {
        if (available == null) {
            available = true;
        }
    }
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Reviews> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<PostsAvailability> postsAvailabilities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_type_id", nullable = false)
    Place_type placeType;

    String street;
    String ward;
    String district;
    String city;
    String fullAddress;
}
