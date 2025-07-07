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
public class PostCreateRequest {

    String title;
    String description;
    String short_description;
//    String accommodation_type;
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal night_price;
    MultipartFile thumbnail;
    int capacity;
    String city;
    String country;
    boolean pet_friendly;
    String owner;
    String place_type_id;
    List<String> amenity_id = new ArrayList<>();


}
