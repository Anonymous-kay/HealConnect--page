package com.healconnect.healconnect.model; // Corrected package

import javafx.beans.value.ObservableValue;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MedicalRecord {
    private int id;
    private int patientId;
    private LocalDate recordDate;
    private String recordType; // e.g., "Diagnosis", "Prescription", "Test Result"
    private String description;
    private String filePath; // Optional path to attached documents
    private int version;
    private LocalDateTime lastUpdated;
    private int updatedBy; // user_id who made the last update

    // Constructor for initial creation (without versioning info)
    public MedicalRecord(int id, int patientId, LocalDate recordDate,
                         String recordType, String description, String filePath) {
        this.id = id;
        this.patientId = patientId;
        this.recordDate = recordDate;
        this.recordType = recordType;
        this.description = description;
        this.filePath = filePath;
        // Default values for versioning if not provided
        this.version = 1;
        this.lastUpdated = LocalDateTime.now();
        this.updatedBy = 0; // Should be set to actual user ID
    }

    // Updated constructor including versioning information
    public MedicalRecord(int id, int patientId, LocalDate recordDate,
                         String recordType, String description,
                         String filePath, int version,
                         LocalDateTime lastUpdated, int updatedBy) {
        this.id = id;
        this.patientId = patientId;
        this.recordDate = recordDate;
        this.recordType = recordType;
        this.description = description;
        this.filePath = filePath;
        this.version = version;
        this.lastUpdated = lastUpdated;
        this.updatedBy = updatedBy;
    }

    // Getters
    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public LocalDate getRecordDate() { return recordDate; }
    public String getRecordType() { return recordType; }
    public String getDescription() { return description; }
    public String getFilePath() { return filePath; }
    public int getVersion() { return version; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public int getUpdatedBy() { return updatedBy; }

    // Setters (added for updating records)
    public void setId(int id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setVersion(int version) { this.version = version; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    public void setUpdatedBy(int updatedBy) { this.updatedBy = updatedBy; }


    @Override
    public String toString() {
        return recordType + " - " + recordDate;
    }

    public boolean hasAttachment() {
        return filePath != null && !filePath.isEmpty();
    }

    public String getOriginalFileName() {
        if (hasAttachment()) {
            // Assuming file_path stores UUID_originalFileName.ext
            int lastUnderscore = filePath.lastIndexOf("_");
            if (lastUnderscore != -1 && lastUnderscore < filePath.length() - 1) {
                return filePath.substring(lastUnderscore + 1);
            }
            return filePath; // Fallback if format is not as expected
        }
        return "";
    }

    public ObservableValue<LocalDate> recordDateProperty() {
        return null;
    }

    public ObservableValue<String> recordTypeProperty() {
        return null;
    }

    public ObservableValue<String> descriptionProperty() {
        return null;
    }

//    public ObservableValue<String> recordTypeProperty() {
//        return null;
//    }
//
//    public ObservableValue<String> descriptionProperty() {
//        return null;
//    }
}
