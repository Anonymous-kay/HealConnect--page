package com.healconnect.healconnect.service; // Corrected package

import com.healconnect.healconnect.dao.UserDAO; // Added import
import com.healconnect.healconnect.model.User; // Added import
import com.healconnect.healconnect.util.AppConfig; // Added import
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileStorageService {
    private final Path fileStorageLocation;

    public FileStorageService(String uploadDir) throws Exception {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) { // Changed to IOException
            throw new Exception("Could not create upload directory: " + uploadDir, ex);
        }
    }

    // Overloaded storeFile method to handle file size check and return unique name
    public String storeFile(byte[] fileData, String originalFileName) throws Exception {
        // Check file size
        int maxSizeMB = AppConfig.getMaxFileSizeMB();
        if (fileData.length > maxSizeMB * 1024 * 1024) {
            throw new Exception("File size exceeds maximum limit of " + maxSizeMB + "MB");
        }

        // Generate unique filename
        String fileExtension = "";
        int dotIndex = originalFileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < originalFileName.length() - 1) {
            fileExtension = originalFileName.substring(dotIndex);
        }
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
        Files.write(targetLocation, fileData);

        return uniqueFileName;
    }

    // Original loadFile method (without access control)
    public byte[] loadFile(String fileName) throws IOException {
        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + fileName);
        }
        return Files.readAllBytes(filePath);
    }

    // Overloaded loadFile method with access control
    public byte[] loadFile(String fileName, int requestingUserId, int recordOwnerId) throws Exception {
        // Verify user has access to this file
        if (!hasFileAccess(requestingUserId, recordOwnerId)) {
            throw new SecurityException("User does not have access to this file.");
        }

        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + fileName);
        }
        return Files.readAllBytes(filePath);
    }

    public void deleteFile(String fileName) throws IOException {
        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
        Files.deleteIfExists(filePath);
    }

    private boolean hasFileAccess(int userId, int recordOwnerId) {
        // In real implementation, check user role and permissions
        // For now, just verify if user is the record owner or an admin
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserById(userId); // Implement this method in UserDAO

        return user != null &&
                (userId == recordOwnerId || "Admin".equals(user.getRole()));
    }
}
