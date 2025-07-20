package com.healconnect.healconnect.model; // Corrected package

import java.time.LocalDate;
import java.util.List;

public class Prescription {
    private int id;
    private int patientId;
    private int providerId;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private List<Medication> medications;
    private String instructions;

    public Prescription(int id, int patientId, int providerId, LocalDate issueDate, LocalDate expiryDate, List<Medication> medications, String instructions) {
        this.id = id;
        this.patientId = patientId;
        this.providerId = providerId;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.medications = medications;
        this.instructions = instructions;
    }

    // Getters
    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getProviderId() { return providerId; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public List<Medication> getMedications() { return medications; }

    //Setters(if needed)
    public void setId(int id) { this.id = id; }
    public void setPatientId(int patientId) {this.patientId = patientId; }
    public void setProviderId(int providerId) {this.providerId = providerId;  }
    public void  setIssueDate(LocalDate issueDate) {this.issueDate = issueDate; }






//
//    // Setters (if needed)
//    public void setId(int id) { this.id = id; }
//    public void setPatientId(int patientId) { this.patientId = patientId; }
//    public void setProviderId(int providerId) { this.providerId = providerId; }
//    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
//    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
//    public void setMedications(List<Medication> medications) { this.medications = medications; }
//    public void setInstructions(String instructions) { this.instructions = instructions; }
}
