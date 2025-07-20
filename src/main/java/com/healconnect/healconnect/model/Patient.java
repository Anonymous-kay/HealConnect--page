package com.healconnect.healconnect.model; // Corrected package

import java.time.LocalDate;
import java.time.Period;

public class Patient {
    private int id;
    private int userId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String address;
    private String medicalHistorySummary;

    public Patient(int id, int userId, String firstName, String lastName,
                   LocalDate dateOfBirth, String phoneNumber,
                   String address, String medicalHistorySummary) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.medicalHistorySummary = medicalHistorySummary;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getMedicalHistorySummary() { return medicalHistorySummary; }
    public void setMedicalHistorySummary(String medicalHistorySummary) { this.medicalHistorySummary = medicalHistorySummary; }

    // Additional methods referenced in controllers
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getAge() {
        if (dateOfBirth != null) {
            return String.valueOf(Period.between(dateOfBirth, LocalDate.now()).getYears());
        }
        return "N/A";
    }

    public String getGender() {
        // This would typically come from the database. Placeholder for now.
        return "Unknown";
    }

    public String getAvatarUrl() {
        // Placeholder for avatar URL. In a real app, this would be stored in DB.
        return null;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
