package com.healconnect.healconnect.dao;

import com.healconnect.healconnect.model.User;
import com.healconnect.healconnect.model.Doctor; // Import the correct Doctor model

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DoctorDAO {
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        // In a real application, this would query the database
        // For demonstration, return some dummy doctors
        doctors.add(new Doctor(101, "Dr. Smith", "hashedpass", "Provider", "Cardiology")); // Assuming specialty is added
        doctors.add(new Doctor(102, "Dr. Jones", "hashedpass", "Provider", "Pediatrics"));
        return doctors;
    }
}
    