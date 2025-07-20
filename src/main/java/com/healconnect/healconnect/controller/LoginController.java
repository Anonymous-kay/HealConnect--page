package com.healconnect.healconnect.controller; // Corrected package

import com.healconnect.healconnect.dao.UserDAO;
import com.healconnect.healconnect.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both username and password.");
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        UserDAO userDAO = new UserDAO();
        User authenticatedUser = userDAO.authenticateUser(username, password);

        if (authenticatedUser != null) {
            try {
                navigateToDashboard(authenticatedUser, event);
            } catch (IOException e) {
                messageLabel.setText("Error navigating to dashboard: " + e.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
                e.printStackTrace();
            }
        } else {
            messageLabel.setText("Invalid username or password.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void navigateToDashboard(User user, ActionEvent event) throws IOException {
        FXMLLoader loader;
        Stage stage = (Stage) usernameField.getScene().getWindow(); // Get the current stage

        String fxmlPath;
        String title;

        if (user.getRole().equalsIgnoreCase("Patient")) {
            fxmlPath = "/com/healconnect/healconnect/patient-dashboard.fxml"; // Corrected path
            title = "HealConnect - Patient Dashboard";
        } else if (user.getRole().equalsIgnoreCase("Provider")) {
            fxmlPath = "/com/healconnect/healconnect/provider-dashboard.fxml"; // Corrected path
            title = "HealConnect - Provider Dashboard";
        } else if (user.getRole().equalsIgnoreCase("Admin")) { // Added Admin role
            fxmlPath = "/com/healconnect/healconnect/admin-dashboard.fxml"; // Assuming admin-dashboard.fxml exists
            title = "HealConnect - Admin Dashboard";
        } else {
            messageLabel.setText("Unknown user role.");
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(loader.load(), 800, 600); // Set a default size

        // Pass the authenticated user to the dashboard controller if needed
        // Example: if (loader.getController() instanceof PatientDashboardController) {
        //     ((PatientDashboardController) loader.getController()).initialize(user);
        // }

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
