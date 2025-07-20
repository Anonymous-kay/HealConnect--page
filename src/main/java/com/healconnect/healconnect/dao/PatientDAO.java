package com.healconnect.healconnect.dao; // Corrected package

import com.healconnect.healconnect.model.Patient;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public List<Patient> getPatientsByProvider(int providerId) {
        List<Patient> patients = new ArrayList<>();
        // TODO: Implement database query to get patients assigned to a provider
        // For now, return dummy data
        patients.add(new Patient(1, 1, "Alice", "Smith", java.time.LocalDate.of(1990, 5, 15), "555-1234", "123 Main St", "No major history"));
        patients.add(new Patient(2, 2, "Bob", "Johnson", java.time.LocalDate.of(1985, 10, 20), "555-5678", "456 Oak Ave", "Allergies to penicillin"));
        return patients;
    }

    public List<Patient> searchPatients(String searchTerm) {
        List<Patient> results = new ArrayList<>();
        // TODO: Implement database query for patient search
        // For now, filter dummy data
        for (Patient patient : getPatientsByProvider(0)) { // Using 0 as dummy providerId
            if (patient.getFirstName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    patient.getLastName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    patient.getPhoneNumber().contains(searchTerm)) {
                results.add(patient);
            }
        }
        return results;
    }
}
