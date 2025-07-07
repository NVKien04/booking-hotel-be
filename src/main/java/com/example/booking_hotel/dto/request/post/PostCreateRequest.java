package com.example.booking_hotel.dto.request.post;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostCreateRequest {

    String title;
    String description;
    String short_description;
    //    String accommodation_type;
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal nightPrice;

    BigDecimal weekendPrice;
    MultipartFile thumbnail;
        MultipartFile[] files;
    int capacity;
    String street;
    String ward;
    String district;
    String city;
    boolean pet_friendly;
    String placeType;
    List<String> amenity_id = new ArrayList<>();
}
