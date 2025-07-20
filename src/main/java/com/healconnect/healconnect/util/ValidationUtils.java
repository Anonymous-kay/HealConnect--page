package com.healconnect.healconnect.util; // Corrected package

import javafx.scene.control.Control;
import javafx.scene.control.DatePicker; // Added import
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl; // Added import
import java.time.LocalDate;
// import java.time.LocalTime; // Not used in provided methods
import java.util.regex.Pattern;

public class ValidationUtils {
    private static final Pattern TIME_PATTERN = Pattern.compile("^(0?[1-9]|1[0-2]):[0-5][0-9]$"); // This pattern is for HH:MM, but not used in current methods

    public static boolean validateRequired(Control field, Label errorLabel, String fieldName) {
        if (field instanceof TextInputControl) {
            if (((TextInputControl)field).getText().trim().isEmpty()) {
                errorLabel.setText(fieldName + " is required.");
                return false;
            }
        } else if (field instanceof DatePicker) {
            if (((DatePicker)field).getValue() == null) {
                errorLabel.setText(fieldName + " is required.");
                return false;
            }
        }
        // Add other control types if needed (e.g., ComboBox)
        return true;
    }

    public static boolean validateDate(DatePicker datePicker, Label errorLabel) {
        if (datePicker.getValue() == null) {
            errorLabel.setText("Please select a date.");
            return false;
        }
        if (datePicker.getValue().isBefore(LocalDate.now())) {
            errorLabel.setText("Please select a future date.");
            return false;
        }
        return true;
    }

    public static boolean validateTimeFormat(String time, Label errorLabel) {
        // This method is provided but not used in AppointmentBookingController's validateForm
        // as it uses ComboBoxes for time components.
        if (!TIME_PATTERN.matcher(time).matches()) {
            errorLabel.setText("Time must be in HH:MM format (e.g., 09:30).");
            return false;
        }
        return true;
    }

    public static void clearError(Label errorLabel) {
        if (errorLabel != null) {
            errorLabel.setText("");
        }
    }
}
