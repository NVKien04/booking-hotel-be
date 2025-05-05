package com.example.booking_hotel.entity;

import com.example.booking_hotel.enums.Accommodation_type;
import com.example.booking_hotel.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Posts extends BaseEntity {
    @ManyToOne
            @JoinColumn(name = "owner_id", nullable = false)
    User owner;
    String title;
    @Column(columnDefinition = "TEXT")
    String description;
    @Column(columnDefinition = "TEXT")
    String short_description;
    @Enumerated(EnumType.STRING)
    @Column(name = "accommodation_type", nullable = false)
    Accommodation_type accommodation_type;
    BigDecimal price;
    String thumbnail;
    int capacity;
    Boolean available;
    boolean pet_friendly;
    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Post_images> post_imagesList;
    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Bookings> bookings;
    @ManyToMany
    @JoinTable(
            name = "post_amenities",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    List<Amenities> amenities = new ArrayList<>();
    String location;


    @PrePersist
    public void ensureActiveIsSet() {
        if (available == null) {
            available = true;
        }
    }

}
