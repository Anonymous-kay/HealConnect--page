package com.healconnect.healconnect.model; // Corrected package

import java.time.LocalDateTime;

public class Consultation {
    private final Appointment appointment;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String notes;

    public Consultation(Appointment appointment) {
        this.appointment = appointment;
        this.startTime = LocalDateTime.now();
    }

    public void endConsultation() {
        this.endTime = LocalDateTime.now();
        // TODO: Save to database
        System.out.println("Consultation ended at: " + endTime);
        System.out.println("Notes: " + notes);
    }

    // Getters and setters
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Appointment getAppointment() { return appointment; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
}
