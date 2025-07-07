package com.example.booking_hotel.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Bất cứ request nào bắt đầu với /uploads/ sẽ map đến thư mục uploads ngoài project
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:uploads/");
    }
}
