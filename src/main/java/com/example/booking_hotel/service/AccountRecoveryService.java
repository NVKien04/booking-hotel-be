package com.example.booking_hotel.service;

import com.example.booking_hotel.dto.request.EmailRequest;
import com.example.booking_hotel.dto.response.VerificationCodeResponse;

public interface AccountRecoveryService {

    VerificationCodeResponse forgotPassword(EmailRequest emailRequest);
}
