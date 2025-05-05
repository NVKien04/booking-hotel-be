package com.example.booking_hotel.service;

import com.example.booking_hotel.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UploadService {
    @NonFinal
    @Value("${file.upload-dir}")
    String uploadDir;

    public String uploadFile(MultipartFile file, String subFolder) {
        try{
            if(file == null|| file.isEmpty()){
                throw new  RuntimeException("file is empty");
            }
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path folderPath = Paths.get(uploadDir, subFolder);
            Files.createDirectories(folderPath);
            Path filePath = folderPath.resolve(fileName);
            file.transferTo(filePath.toFile());
            return "http://localhost:8081/api/post/" + subFolder + "/" + fileName;
        }
        catch(Exception e){
            throw new RuntimeException("Failed to up load files:" + e.getMessage() );
        }

    }



}
