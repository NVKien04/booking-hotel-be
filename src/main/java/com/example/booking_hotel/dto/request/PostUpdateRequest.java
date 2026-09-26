package com.example.booking_hotel.dto.request;

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
public class PostUpdateRequest {

    String title;
    String short_description;

    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal nightPrice;

    BigDecimal weekendPrice;
    MultipartFile thumbnail;
    MultipartFile[] files;
    Integer capacity;
    String addressDetail;
    String district;
    String districtId;
    String city;
    String cityId;
    Boolean pet_friendly;
    Boolean petFriendly;
    String placeType;
    List<String> amenity_id = new ArrayList<>();
    List<String> amenityIds = new ArrayList<>();
    Integer bedrooms;
    Integer bathrooms;
    Integer beds;

    public String getEffectiveCityId() {
        return cityId != null && !cityId.isBlank() ? cityId : city;
    }

    public String getEffectiveDistrictId() {
        return districtId != null && !districtId.isBlank() ? districtId : district;
    }

    public List<String> getEffectiveAmenityIds() {
        if (amenityIds != null && !amenityIds.isEmpty()) {
            return amenityIds;
        }
        return amenity_id != null ? amenity_id : new ArrayList<>();
    }

    public Boolean getEffectivePetFriendly() {
        if (petFriendly != null) return petFriendly;
        return pet_friendly;
    }
}
