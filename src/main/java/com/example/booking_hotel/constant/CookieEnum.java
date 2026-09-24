package com.example.booking_hotel.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CookieEnum {
    REFRESH_TOKEN("refreshToken"),
    LOGGED_IN("loggedIn");

    public static final String REFRESH_TOKEN_COOKIE = "refreshToken";
    public static final String LOGGED_IN_COOKIE = "loggedIn";

    private final String cookieName;
}
