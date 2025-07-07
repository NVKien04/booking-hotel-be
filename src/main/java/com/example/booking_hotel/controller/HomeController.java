package com.example.booking_hotel.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@AllArgsConstructor
@RequestMapping("/home")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeController {}
