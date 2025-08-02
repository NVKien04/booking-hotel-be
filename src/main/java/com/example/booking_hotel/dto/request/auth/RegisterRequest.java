package com.example.booking_hotel.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterRequest {
    @NotBlank(message = "Email không được để trống")
    @Pattern(
            regexp = "^[a-zA-Z0-9]+$",
            message = "Username chỉ được chứa chữ và số, không chứa khoảng trắng hoặc ký tự đặc biệt"
    )
    String username;
    @NotBlank(message = "Email không được để trống")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "Email phải có định dạng hợp lệ")
    String email;
    @Size(min = 8, message = "Password phải từ 8 kí tự trở lên")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&^#()_+=|<>\\[\\]{}~.,:;\"'-]).{8,}$",
            message = "Mật khẩu phải chứa ít nhất 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt"
    )
    String password;
}
