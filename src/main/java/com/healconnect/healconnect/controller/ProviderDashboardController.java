package com.healconnect.healconnect.controller; // Corrected package

import com.healconnect.healconnect.dao.AppointmentDAO;
import com.healconnect.healconnect.dao.PatientDAO;
import com.healconnect.healconnect.model.Appointment;
import com.healconnect.healconnect.model.Patient;
import com.healconnect.healconnect.model.User;
import com.healconnect.healconnect.util.ActionButtonCellFactory; // Corrected import
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox; // Added import
import javafx.stage.Stage; // Added import
import javafx.fxml.FXMLLoader; // Added import
import javafx.scene.Scene; // Added import

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ProviderDashboardController {
    @FXML private Label todaysAppointmentsLabel;
    @FXML private Label messagesLabel;
    @FXML private Label pendingRecordsLabel;
    @FXML private TableView<Appointment> appointmentsTableView;
    @FXML private TableView<Patient> patientsTableView;
    @FXML private TextField patientSearchField;
    @FXML private Button telemedicineBtn;

    private User currentProvider;

    public void initialize(User provider) {
        this.currentProvider = provider;
        loadDashboardData();
        setupAppointmentsTable();
        setupPatientsTable();
    }

    private void loadDashboardData() {
        // Load today's appointment count
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        int todayAppointments = appointmentDAO.getTodaysAppointmentsCount(currentProvider.getId());
        todaysAppointmentsLabel.setText(String.valueOf(todayAppointments));

        // Load other dashboard stats (placeholders)
        messagesLabel.setText("3"); // TODO: Replace with actual count from messages service
        pendingRecordsLabel.setText("5"); // TODO: Replace with actual count from records service
    }

    private void setupAppointmentsTable() {
        // Define columns for appointmentsTableView (assuming FXML or create dynamically)
        // Example:
        TableColumn<Appointment, String> patientNameCol = new TableColumn<>("Patient");
        patientNameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPatientName()));

        TableColumn<Appointment, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAppointmentDateTime().toLocalTime().toString()));

        TableColumn<Appointment, String> reasonCol = new TableColumn<>("Reason");
        reasonCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getReason()));

        appointmentsTableView.getColumns().clear(); // Clear existing columns
        appointmentsTableView.getColumns().addAll(patientNameCol, timeCol, reasonCol);

        // Load today's appointments
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        List<Appointment> appointments = appointmentDAO.getTodaysAppointments(currentProvider.getId());
        appointmentsTableView.setItems(FXCollections.observableArrayList(appointments));

        // Setup action buttons
        TableColumn<Appointment, Integer> actionsCol = new TableColumn<>("Actions"); // Re-create if not in FXML
        actionsCol.setCellFactory(new ActionButtonCellFactory<>(
                this::handleViewAppointment,
                this::handleStartAppointment,
                this::handleCancelAppointment
        ));
        appointmentsTableView.getColumns().add(actionsCol);
    }

    private void setupPatientsTable() {
        // Define columns for patientsTableView (assuming FXML or create dynamically)
        // Example:
        TableColumn<Patient, String> patientNameCol = new TableColumn<>("Name");
        patientNameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFullName()));

        TableColumn<Patient, String> dobCol = new TableColumn<>("DOB");
        dobCol.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty<>(cellData.getValue().getDateOfBirth());
        });

        TableColumn<Patient, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPhoneNumber()));

        patientsTableView.getColumns().clear(); // Clear existing columns
        patientsTableView.getColumns().addAll(patientNameCol, dobCol, phoneCol);

        // Load all patients
        PatientDAO patientDAO = new PatientDAO();
        List<Patient> patients = patientDAO.getPatientsByProvider(currentProvider.getId());
        patientsTableView.setItems(FXCollections.observableArrayList(patients));

        // Setup action buttons
        TableColumn<Patient, Integer> actionsCol = new TableColumn<>("Actions"); // Re-create if not in FXML
        actionsCol.setCellFactory(new ActionButtonCellFactory<>(
                this::handleViewPatient,
                this::handleEditPatient,
                this::handleMessagePatient
        ));
        patientsTableView.getColumns().add(actionsCol);
    }

    @FXML
    private void handlePatientSearch() {
        String searchTerm = patientSearchField.getText().trim();
        PatientDAO patientDAO = new PatientDAO();
        if (!searchTerm.isEmpty()) {
            List<Patient> results = patientDAO.searchPatients(searchTerm);
            patientsTableView.setItems(FXCollections.observableArrayList(results));
        } else {
            // Reload all patients if search is empty
            setupPatientsTable();
        }
    }

    @FXML
    private void handleViewCalendar() {
        // TODO: Implement calendar view
        System.out.println("View Calendar clicked.");
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Calendar view not yet implemented.");
        alert.showAndWait();
    }

    @FXML
    private void handleAddPatient() {
        // TODO: Implement logic to open patient registration dialog
        System.out.println("Add Patient clicked.");
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Add Patient functionality not yet implemented.");
        alert.showAndWait();
    }

    @FXML
    private void handleTelemedicine() {
        // TODO: Implement logic to launch telemedicine interface
        System.out.println("Telemedicine button clicked.");
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Telemedicine launch functionality not yet implemented.");
        alert.showAndWait();
    }

    // Appointment action handlers
    private void handleViewAppointment(Appointment appointment) {
        // TODO: Show appointment details dialog
        System.out.println("View Appointment: " + appointment.getReason());
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Viewing appointment details for: " + appointment.getPatientName());
        alert.showAndWait();
    }

    private void handleStartAppointment(Appointment appointment) {
        // Launch telemedicine interface for this appointment
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/healconnect/healconnect/telemedicine-view.fxml"));
            VBox telemedicineRoot = loader.load();
            TelemedicineController telemedicineController = loader.getController();
            // Pass the patient and appointment to the telemedicine controller
            PatientDAO patientDAO = new PatientDAO(); // Assuming you can get patient by ID
            Patient patient = patientDAO.searchPatients("").stream() // Dummy search to get a patient
                    .filter(p -> p.getId() == appointment.getPatientId())
                    .findFirst().orElse(null);

            if (patient != null) {
                telemedicineController.initialize(patient, appointment);
                Stage telemedicineStage = new Stage();
                telemedicineStage.setTitle("Telemedicine Consultation with " + patient.getFullName());
                telemedicineStage.setScene(new Scene(telemedicineRoot, 1000, 700));
                telemedicineStage.show();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Could not find patient details for this appointment.");
                errorAlert.showAndWait();
            }

        } catch (IOException e) {
            System.err.println("Error loading telemedicine view: " + e.getMessage());
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to open telemedicine session.");
            errorAlert.showAndWait();
        }
    }

    private void handleCancelAppointment(Appointment appointment) {
        // TODO: Show confirmation and cancel appointment
        System.out.println("Cancel Appointment: " + appointment.getReason());
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel this appointment?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Logic to update appointment status in DB
                System.out.println("Appointment cancelled.");
                loadDashboardData(); // Refresh data
                setupAppointmentsTable(); // Refresh table
            }
        });
    }

    // Patient action handlers
    private void handleViewPatient(Patient patient) {
        // Open medical records view for this patient
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/healconnect/healconnect/medical-records-view.fxml"));
            VBox medicalRecordsRoot = loader.load();
            MedicalRecordsController medicalRecordsController = loader.getController();
            medicalRecordsController.initialize(patient.getId()); // Pass patient ID
            Stage medicalRecordsStage = new Stage();
            medicalRecordsStage.setTitle("Medical Records for " + patient.getFullName());
            medicalRecordsStage.setScene(new Scene(medicalRecordsRoot, 800, 600));
            medicalRecordsStage.show();
        } catch (IOException e) {
            System.err.println("Error loading medical records view: " + e.getMessage());
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to open medical records.");
            errorAlert.showAndWait();
        }
    }

    private void handleEditPatient(Patient patient) {
        // TODO: Open patient edit dialog
        System.out.println("Edit Patient: " + patient.getFullName());
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Edit Patient functionality not yet implemented.");
        alert.showAndWait();
    }

    private void handleMessagePatient(Patient patient) {
        // TODO: Open chat with patient
        System.out.println("Message Patient: " + patient.getFullName());
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Messaging functionality not yet implemented.");
        alert.showAndWait();
    }
}
