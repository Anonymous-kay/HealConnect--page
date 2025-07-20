package com.healconnect.healconnect; // Keep in root for simple execution

import com.healconnect.healconnect.dao.UserDAO;

public class UserCreationTest {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        // Example: Create an admin user
        boolean success = userDAO.createUser("admin", "password123", "Admin");
        if (success) {
            System.out.println("User 'admin' created successfully!");
        } else {
            System.out.println("User 'admin' creation failed.");
        }

        // Example: Create a patient user
        success = userDAO.createUser("patient1", "patientpass", "Patient");
        if (success) {
            System.out.println("User 'patient1' created successfully!");
        } else {
            System.out.println("User 'patient1' creation failed.");
        }

        // Example: Create a provider user
        success = userDAO.createUser("doctor1", "doctorpass", "Provider");
        if (success) {
            System.out.println("User 'doctor1' created successfully!");
        } else {
            System.out.println("User 'doctor1' creation failed.");
        }
    }
}
