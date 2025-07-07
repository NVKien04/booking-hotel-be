package com.example.booking_hotel.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UploadService {
    @NonFinal
    @Value("${file.upload-dir}")
    String uploadDir;

    public String uploadFile(MultipartFile file, String subFolder, String controller) {
        try {
            if (file == null || file.isEmpty()) {
                throw new RuntimeException("file is empty");
            }
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path folderPath = Paths.get(uploadDir, subFolder);
            Files.createDirectories(folderPath);
            Path filePath = folderPath.resolve(fileName);
            file.transferTo(filePath.toFile());
            return "http://localhost:8081/api/img" + "/" + subFolder + "/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to up load files:" + e.getMessage());
        }
    }
}
