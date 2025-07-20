package com.healconnect.healconnect.controller;

import com.healconnect.healconnect.model.User;

// Placeholder for Doctor model, as it's used in ComboBox
// In a real app, this would be a proper model class
public class Dr extends User { // Assuming Doctor extends User or is a separate model
    public Dr(int id, String username, String passwordHash, String role) {
        super(id, username, passwordHash, role);
    }
    public String getLastName() {
        // Placeholder: In a real app, you'd fetch this from a 'doctors' table
        return getUsername(); // Using username as a placeholder for last name
    }
    @Override
    public String toString() {
        return getUsername(); // Display username in ComboBox
    }
}
