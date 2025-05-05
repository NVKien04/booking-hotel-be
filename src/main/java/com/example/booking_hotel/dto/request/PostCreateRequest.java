package com.example.booking_hotel.dto.request;

import com.example.booking_hotel.enums.Accommodation_type;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
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
    BigDecimal price;
    MultipartFile thumbnail;
    int capacity;
    boolean pet_friendly;
    String locations;
    String owner;

//    List<String> amenities;
//    List<PostImgRequest> post_imagesList;


}
