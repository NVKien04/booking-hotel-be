package com.example.booking_hotel.service;

import com.example.booking_hotel.dto.request.auth.IntrospectRequest;
import com.example.booking_hotel.dto.request.auth.LoginRequest;
import com.example.booking_hotel.dto.request.auth.RegisterRequest;
import com.example.booking_hotel.dto.response.auth.AuthResponse;
import com.example.booking_hotel.dto.response.auth.IntrospectResponse;
import com.example.booking_hotel.dto.response.user.UserResponse;
import com.example.booking_hotel.entity.User;
import com.example.booking_hotel.enums.Role;
import com.example.booking_hotel.exception.AppException;
import com.example.booking_hotel.exception.ErrorCode;
import com.example.booking_hotel.mapper.UserMapper;
import com.example.booking_hotel.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {

    String SIGNER_KEY =" ISaeBKjUzdPg8OWFRm5Cq42I4XZlefm2s/6nxKOpC+j8aHQ16dYfX6ON6AAfu3Te";


    UserRepository userRepository;
    UserMapper userMapper;


    public UserResponse registerRenter(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw  new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.mapToUser(registerRequest);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.RENTER);
        var rs = userRepository.save(user);
        log.info("kien");
        return userMapper.mapToUserResponse(rs);
    }

    public AuthResponse authenticated(LoginRequest loginRequest) {
        User user = userRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticate = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        if(!authenticate){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        var token = generateToken(user);

        return AuthResponse.builder()
                .authenticated(true)
                .token(token)
                .build();

    }

    public IntrospectResponse introspectResponse(IntrospectRequest introspectRequest) throws JOSEException, ParseException {

        String token = introspectRequest.getToken().toString();

        boolean invalidated = true;

        try{
            verifyToken(token);
        }
        catch(JOSEException | AppException | ParseException e){
            invalidated = false;
        }

        return IntrospectResponse.builder()
                .valid(true)
                .build();
    }


    private String generateToken(User user){

        JWSHeader  jwsHeader =new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("bookingClone")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(60, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .claim("scope", user.getRole().toString())
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload   payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try{
            jwsObject.sign(new MACSigner(SIGNER_KEY));
            return jwsObject.serialize();
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }


    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT =SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(jwsVerifier);

        if((verified && expiryTime.after(new Date()))){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return SignedJWT.parse(token);
    }








}
