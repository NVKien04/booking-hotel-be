package com.example.booking_hotel.utils;

import java.util.concurrent.TimeUnit;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.example.booking_hotel.constant.CookieEnum;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CookieHelper {

  @NonFinal
  @Value("${jwt.refresh-token.expiry-in-days:20}")
  long refreshTokenExpirationInDays;

  public void setAuthCookies(HttpServletResponse response, String refreshToken) {
    long maxAgeInSeconds = TimeUnit.DAYS.toSeconds(refreshTokenExpirationInDays);
    setCookie(response, CookieEnum.REFRESH_TOKEN.getCookieName(), refreshToken, maxAgeInSeconds, true);
    setCookie(response, CookieEnum.LOGGED_IN.getCookieName(), "true", maxAgeInSeconds, false);
  }

  public void clearAuthCookies(HttpServletResponse response) {
    setCookie(response, CookieEnum.REFRESH_TOKEN.getCookieName(), "", 0, true);
    setCookie(response, CookieEnum.LOGGED_IN.getCookieName(), "", 0, false);
  }

  public void setCookie(
      HttpServletResponse response,
      String name,
      String value,
      long maxAgeInSeconds,
      boolean httpOnly) {
    ResponseCookie cookie = ResponseCookie.from(name, value)
        .httpOnly(httpOnly)
        .secure(false)
        .path("/")
        .maxAge(maxAgeInSeconds)
        .sameSite("Lax")
        .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }

}
