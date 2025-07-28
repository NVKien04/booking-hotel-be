package com.example.booking_hotel.dto.request.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostUpdateRequest {

    String title;
    String short_description;
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal nightPrice;
    BigDecimal weekendPrice;
    MultipartFile thumbnail;
        MultipartFile[] files;
    int capacity;
    String addressDetail;
    String district;
    String city;
    boolean pet_friendly;
    String placeType;
    List<String> amenity_id = new ArrayList<>();
    int bedrooms;
    int bathrooms;
    int beds;
}
