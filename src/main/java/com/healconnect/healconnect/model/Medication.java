package com.healconnect.healconnect.model; // Corrected package

public class Medication {
    private String name;
    private String dosage;
    private String frequency;
    private String duration;

    public Medication(String name, String dosage, String frequency, String duration) {
        this.name = name;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
    }

    // Getters
    public String getName() { return name; }
    public String getDosage() { return dosage; }
    public String getFrequency() { return frequency; }
    public String getDuration() { return duration; }

    // Setters (if needed)
    public void setName(String name) { this.name = name; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public void setDuration(String duration) { this.duration = duration; }

    @Override
    public String toString() {
        return name + " (" + dosage + ", " + frequency + ")";
    }
}
