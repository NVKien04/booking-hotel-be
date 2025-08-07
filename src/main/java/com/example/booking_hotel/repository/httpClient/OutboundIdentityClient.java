package com.example.booking_hotel.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.booking_hotel.dto.request.auth.ExChangeTokenRequest;
import com.example.booking_hotel.dto.response.auth.ExChangeTokenResponse;

import feign.QueryMap;

@FeignClient(name = "outbound-identity", url = "https://oauth2.googleapis.com")
public interface OutboundIdentityClient {
    @PostMapping(value = "/token", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ExChangeTokenResponse exchangeToken(@QueryMap ExChangeTokenRequest exChangeTokenRequest);
}
