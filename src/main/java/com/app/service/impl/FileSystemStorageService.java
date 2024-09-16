package com.app.service.impl;

import com.app.config.StorageProperties;
import com.app.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {
    private final Path fileDir;
    private final Path uploadDir;
    private String  fileUrl;
    private final String host;
    private final Integer port;
    private final String username;
    private final String password;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.fileDir = Paths.get(properties.getFilesDir());
        this.uploadDir = Paths.get(properties.getFilesDir() + "/" + properties.getUploadDir());
        this.host=properties.getHost();
        this.port=properties.getPort();
        this.username=properties.getUsername();
        this.password=properties.getPassword();
    }
    @Override
    public void init() {
        try {
            Files.createDirectories(uploadDir);
        }catch (IOException e) {
//            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public void store(String fileName, MultipartFile file) {

    }

    @Override
    public String getFileUrl() {
        return null;
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
