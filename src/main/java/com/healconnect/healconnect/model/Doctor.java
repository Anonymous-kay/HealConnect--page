package com.healconnect.healconnect.model;

public class Doctor extends User {
    private String specialty; // Example: Add a specialty field

    public Doctor(int id, String username, String passwordHash, String role, String specialty) {
        super(id, username, passwordHash, role);
        this.specialty = specialty;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getLastName() {
        // This is a placeholder. In a real app, you'd parse from username or have separate fields.
        String[] parts = getUsername().split(" ");
        if (parts.length > 1) {
            return parts[parts.length - 1];
        }
        return getUsername();
    }

    @Override
    public String toString() {
        return getUsername() + (specialty != null && !specialty.isEmpty() ? " (" + specialty + ")" : "");
    }
}
    