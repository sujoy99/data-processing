package com.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("storage")
public class StorageProperties {
    private final String filesDir = "/app";
    private final String uploadDir = "uploads";
    private final String host = "192.168.0.106";
    private final Integer port = 22;
    private final String username = "rt";
    private final String password = "centos";

    public String getFilesDir() {
        return filesDir;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
