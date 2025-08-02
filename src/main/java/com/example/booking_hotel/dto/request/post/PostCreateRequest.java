package com.example.booking_hotel.dto.request.post;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @NotBlank(message = "Mô tả ngắn không được để trống")
    private String short_description;

    @NotNull(message = "Giá ngày thường không được null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá ngày thường phải lớn hơn 0")
    private BigDecimal nightPrice;

    @NotNull(message = "Giá cuối tuần không được null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá cuối tuần phải lớn hơn 0")
    private BigDecimal weekendPrice;

    private MultipartFile thumbnail;

    private MultipartFile[] files;

    @NotNull(message = "Sức chứa không được để trống")
    @Min(value = 1, message = "Sức chứa phải lớn hơn 0")
    private Integer capacity;

    @NotBlank(message = "Địa chỉ chi tiết không được để trống")
    private String addressDetail;

    @NotBlank(message = "Mã quận/huyện không được để trống")
    private String districtId;

    @NotBlank(message = "Mã thành phố không được để trống")
    private String cityId;

    private boolean petFriendly;

    @NotBlank(message = "Loại chỗ ở không được để trống")
    private String placeType;

    private List<String> amenityIds = new ArrayList<>();

    @NotNull(message = "Số phòng ngủ không được để trống")
    @Min(value = 0, message = "Số phòng ngủ phải >= 0")
    private Integer bedrooms;

    @NotNull(message = "Số phòng tắm không được để trống")
    @Min(value = 0, message = "Số phòng tắm phải >= 0")
    private Integer bathrooms;

    @Min(value = 0, message = "Số giường phải >= 0")
    private int beds;
}
