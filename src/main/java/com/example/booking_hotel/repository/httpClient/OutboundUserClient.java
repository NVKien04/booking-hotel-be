package com.example.booking_hotel.repository.httpClient;

import com.example.booking_hotel.dto.request.auth.ExChangeTokenRequest;
import com.example.booking_hotel.dto.response.auth.ExChangeTokenResponse;
import com.example.booking_hotel.dto.response.auth.OutboundUserResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "outbound-info-client",  url = "https://www.googleapis.com")
public interface OutboundUserClient {
    @GetMapping(value = "/oauth2/v1/userinfo")
    OutboundUserResponse getUserInfo(@RequestParam("alt") String alt,
                                     @RequestParam("access_token") String accessToken
    );
    }
