package com.app.service.impl;


import com.app.operation.DataController;
import com.app.service.FileStorageService;
import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.app.util.FileUploadUtil.getFileExtension;

@Component("remoteFileStorage")
public class RemoteFileStorageServiceImpl implements FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(RemoteFileStorageServiceImpl.class);
    private static final String REMOTE_HOST = "192.168.0.106";
    private static final String REMOTE_USER = "rt";
    private static final String REMOTE_PASSWORD = "centos";
    private static final String REMOTE_DIRECTORY = "/var/www/app/uploads/";

    @Override
    public String storeFile(MultipartFile file) {
        JSch jsch = new JSch();
        String fileName = UUID.randomUUID().toString() + '.' + getFileExtension(file);
        ChannelSftp channelSftp = null;
        Session session = null;
        try {
            session = jsch.getSession(REMOTE_USER, REMOTE_HOST, 22);
            session.setPassword(REMOTE_PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            if (!directoryExists(channelSftp, REMOTE_DIRECTORY)) {
                log.info("========================failed to create directory in remote server==================");
            }

            try (InputStream inputStream = file.getInputStream()) {
                channelSftp.put(inputStream, REMOTE_DIRECTORY + fileName);
            }

            channelSftp.disconnect();
            session.disconnect();

            return fileName;
        } catch (SftpException | IOException | JSchException e) {
            e.printStackTrace();
            return "Error occurred while uploading file: " + e.getMessage();
        } finally {
            // Close resources in a finally block
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }

    // Method to check if a directory exists on the remote server
    private boolean directoryExists(ChannelSftp channelSftp, String directoryPath) {
        try {
            SftpATTRS attrs = channelSftp.stat(directoryPath);
            return attrs != null && attrs.isDir();
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                // Directory doesn't exist, try creating it recursively
                try {
                    // Split the directory path into individual directory names
                    String[] directories = directoryPath.split("/");
                    // Initialize the current directory path
                    String currentDirectory = "/";
                    // Iterate through each directory and create if it doesn't exist
                    for (String directory : directories) {
                        // Skip empty directory names
                        if (!directory.isEmpty()) {
                            currentDirectory = currentDirectory + directory + "/";
                            try {
                                channelSftp.mkdir(currentDirectory);
                                System.out.println("Remote directory created: " + currentDirectory);
                            } catch (SftpException ex) {
                                // Ignore if the directory already exists
                                if (ex.id != ChannelSftp.SSH_FX_FAILURE) {
                                    ex.printStackTrace();
                                    return false; // Failed to create directory
                                }
                            }
                        }
                    }
                    return true; // Directory created successfully
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false; // Error occurred while creating directory
                }
            } else {
                e.printStackTrace();
                return false; // Error occurred while checking directory existence
            }
        }
    }


    // Method to check if a directory exists on the remote server
/*    private boolean directoryExists(ChannelSftp channelSftp, String directoryPath) {
        try {
            SftpATTRS attrs = channelSftp.stat(directoryPath);
            return attrs != null && attrs.isDir();
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                // Directory doesn't exist, try creating it recursively
                try {
                    channelSftp.mkdir(directoryPath);
                    return true; // Directory created successfully
                } catch (SftpException ex) {
                    ex.printStackTrace();
                    return false; // Failed to create directory
                }
            } else {
                e.printStackTrace();
                return false; // Error occurred while checking directory existence
            }
        }
    }*/
}
