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

    private String title;
    String  short_description;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal nightPrice;

    private BigDecimal weekendPrice;

    private MultipartFile thumbnail;

    private MultipartFile[] files;

    private int capacity;

    private String addressDetail;

    private String districtId;

    private String cityId;

    private boolean petFriendly;

    private String placeType;

    private List<String> amenityIds = new ArrayList<>();

    private int bedrooms;

    private int bathrooms;

    private int beds;
}
