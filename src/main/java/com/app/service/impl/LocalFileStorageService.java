package com.app.service.impl;

import com.app.service.FileStorageService;
import com.app.util.FileUploadUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.app.util.FileUploadUtil.getFileExtension;

@Component("localFileStorage")
public class LocalFileStorageService implements FileStorageService {

    private final Path fileStorageLocation = Paths.get("uploads");

    @Override
    public String storeFile(MultipartFile file) {
        String fileName = UUID.randomUUID().toString() + '.' + getFileExtension(file);

        try {
            String uploadDir = "uploads/";
            FileUploadUtil.saveFile(uploadDir, fileName, file);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

}
