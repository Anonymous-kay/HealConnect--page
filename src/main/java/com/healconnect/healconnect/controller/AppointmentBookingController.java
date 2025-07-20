package com.healconnect.healconnect.controller; // Corrected package

import com.healconnect.healconnect.dao.AppointmentDAO; // Added import
import com.healconnect.healconnect.model.Appointment; // Added import
import com.healconnect.healconnect.model.Patient; // Added import
import com.healconnect.healconnect.util.ValidationUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage; // Added import
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter; // Added import
import java.util.Collection;

public class AppointmentBookingController {
    @FXML private ComboBox<Dr> providerComboBox;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> hourComboBox;
    @FXML private ComboBox<String> minuteComboBox;
    @FXML private ComboBox<String> amPmComboBox;
    @FXML private TextArea reasonTextArea;
    @FXML private ToggleGroup urgencyGroup;
    @FXML private Label errorLabel;

    private Patient currentPatient; // Assuming a patient is booking

    public void initialize(Patient patient) { // Initialize with current patient
        this.currentPatient = patient;
        loadDoctors();
        setupTimeControls();
        datePicker.setValue(LocalDate.now()); // Set default date to today
    }

    private void loadDoctors() {
        DoctorDAO doctorDAO = new DoctorDAO();
        Collection<? extends Dr> doctors = doctorDAO.getAllDoctors();
        providerComboBox.setItems(FXCollections.observableArrayList(doctors));

        // Set default selection
        if (!doctors.isEmpty()) {
            providerComboBox.getSelectionModel().select(0);
        }
    }

    private void setupTimeControls() {
        // Populate hour combo (1-12)
        for (int i = 1; i <= 12; i++) {
            hourComboBox.getItems().add(String.format("%02d", i));
        }

        // Populate minute combo (00, 15, 30, 45)
        minuteComboBox.getItems().addAll("00", "15", "30", "45");

        // Populate AM/PM combo
        amPmComboBox.getItems().addAll("AM", "PM");

        // Select defaults
        LocalTime now = LocalTime.now();
        int currentHour12 = now.getHour() % 12;
        if (currentHour12 == 0) currentHour12 = 12; // Convert 0 to 12 for 12-hour format
        hourComboBox.getSelectionModel().select(String.format("%02d", currentHour12));

        int currentMinute = now.getMinute();
        if (currentMinute >= 0 && currentMinute < 15) minuteComboBox.getSelectionModel().select("00");
        else if (currentMinute >= 15 && currentMinute < 30) minuteComboBox.getSelectionModel().select("15");
        else if (currentMinute >= 30 && currentMinute < 45) minuteComboBox.getSelectionModel().select("30");
        else minuteComboBox.getSelectionModel().select("45");

        amPmComboBox.getSelectionModel().select(now.getHour() < 12 ? "AM" : "PM");
    }

    @FXML
    private void handleBookAppointment() {
        if (validateForm()) {
            // Get form values
            Dr doctor = providerComboBox.getValue();
            LocalDate date = datePicker.getValue();
            String hour = hourComboBox.getValue();
            String minute = minuteComboBox.getValue();
            String amPm = amPmComboBox.getValue();
            String reason = reasonTextArea.getText();
            // Assuming urgencyGroup is properly set up with UserData
            String urgency = (urgencyGroup.getSelectedToggle() != null) ?
                    urgencyGroup.getSelectedToggle().getUserData().toString() : "Normal";

            // Convert to LocalDateTime
            LocalTime time = LocalTime.parse(hour + ":" + minute + " " + amPm,
                    DateTimeFormatter.ofPattern("hh:mm a"));
            LocalDateTime appointmentDateTime = LocalDateTime.of(date, time);

            // Create and save appointment
            Appointment newAppointment = new Appointment(
                    0, // ID will be set by DB
                    currentPatient.getId(),
                    doctor.getId(),
                    appointmentDateTime,
                    reason,
                    "Scheduled" // Initial status
            );

            AppointmentDAO appointmentDAO = new AppointmentDAO();
            if (appointmentDAO.createAppointment(newAppointment)) {
                showConfirmation("Appointment Booked",
                        "Your appointment with Dr. " + doctor.getLastName() +
                                " has been scheduled for " + appointmentDateTime.format(
                                DateTimeFormatter.ofPattern("EEEE, MMMM d 'at' h:mm a")));
            } else {
                errorLabel.setText("Failed to book appointment. Please try again.");
            }
        }
    }

    private boolean validateForm() {
        boolean isValid = true;

        // Clear previous errors
        ValidationUtils.clearError(errorLabel);

        // Validate provider
        if (providerComboBox.getValue() == null) {
            errorLabel.setText("Please select a healthcare provider.");
            isValid = false;
        }

        // Validate date
        if (!ValidationUtils.validateDate(datePicker, errorLabel)) {
            isValid = false;
        }

        // Validate time components
        if (hourComboBox.getValue() == null || minuteComboBox.getValue() == null ||
                amPmComboBox.getValue() == null) {
            errorLabel.setText("Please select a valid time.");
            isValid = false;
        }

        // Validate reason
        if (!ValidationUtils.validateRequired(reasonTextArea, errorLabel, "Reason")) {
            isValid = false;
        }

        return isValid;
    }

    @FXML
    private void handleCancel() {
        // Get the current stage and close it
        Stage stage = (Stage) reasonTextArea.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    private void showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        // Close the booking window after confirmation
        handleCancel();
    }
}
