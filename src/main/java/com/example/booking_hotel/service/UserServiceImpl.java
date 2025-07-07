package com.example.booking_hotel.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.booking_hotel.dto.response.user.AvatarResponse;
import com.example.booking_hotel.dto.response.user.UserResponse;
import com.example.booking_hotel.entity.User;
import com.example.booking_hotel.exception.AppException;
import com.example.booking_hotel.exception.ErrorCode;
import com.example.booking_hotel.mapper.UserMapper;
import com.example.booking_hotel.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    UploadService uploadService;
    UserMapper userMapper;

    @NonFinal
    @Value("${file.upload-user}")
    String UPLOAD_USER;

    @Override
    public UserResponse getInfoUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.mapToUserResponse(user);
    }

    @Override
    public AvatarResponse addAvatar(String idUser, MultipartFile file) {
        User user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setAvatar_img(uploadService.uploadFile(file, UPLOAD_USER, "user"));
        var rs = userRepository.save(user);
        return AvatarResponse.builder().url(rs.getAvatar_img()).build();
    }
}
