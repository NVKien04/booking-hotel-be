package com.example.booking_hotel.controller.HomePage;

import com.example.booking_hotel.dto.response.ApiResponse;
import com.example.booking_hotel.dto.response.PostResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/home")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomePageController {

    @GetMapping("")
    ApiResponse<List<PostResponse>> home(){

        return null;
    }






}
