package com.example.booking_hotel.controller;


import com.example.booking_hotel.dto.request.RegisterRequest;
import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.UserResponse;
import com.example.booking_hotel.service.AuthService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/home")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeController {

}
