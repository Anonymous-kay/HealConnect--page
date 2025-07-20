package com.healconnect.healconnect.view; // Corrected package

import com.healconnect.healconnect.model.Medication;
import com.healconnect.healconnect.model.Patient;
import com.healconnect.healconnect.model.Prescription;
import com.healconnect.healconnect.model.User; // Added import for currentProvider
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets; // Added import
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory; // Added import
import javafx.scene.layout.*;
import java.time.LocalDate;

public class PrescriptionDialog extends Dialog<Prescription> {

    private final ObservableList<Medication> medications = FXCollections.observableArrayList();
    private final Patient patient;
    private User currentProvider; // Placeholder for the current logged-in provider

    public PrescriptionDialog(Patient patient) {
        this.patient = patient;
        setTitle("Create Prescription for " + patient.getFullName());
        setResizable(true);

        // Placeholder for current provider. In a real app, this would be passed in.
        this.currentProvider = new User(100, "Dr. Placeholder", "pass", "Provider");

        // Create form elements
        TableView<Medication> medsTable = new TableView<>(medications);
        medsTable.setPlaceholder(new Label("No medications added yet."));

        // Configure table columns
        TableColumn<Medication, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(120);

        TableColumn<Medication, String> dosageCol = new TableColumn<>("Dosage");
        dosageCol.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        dosageCol.setPrefWidth(80);

        TableColumn<Medication, String> frequencyCol = new TableColumn<>("Frequency");
        frequencyCol.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        frequencyCol.setPrefWidth(100);

        TableColumn<Medication, String> durationCol = new TableColumn<>("Duration");
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationCol.setPrefWidth(80);

        medsTable.getColumns().addAll(nameCol, dosageCol, frequencyCol, durationCol);
        medsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        Button addMedButton = new Button("Add Medication");
        addMedButton.setOnAction(e -> showAddMedicationDialog());

        Button removeMedButton = new Button("Remove Selected");
        removeMedButton.setOnAction(e -> {
            Medication selectedMed = medsTable.getSelectionModel().getSelectedItem();
            if (selectedMed != null) {
                medications.remove(selectedMed);
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a medication to remove.").showAndWait();
            }
        });

        HBox medButtons = new HBox(5, addMedButton, removeMedButton);

        DatePicker expiryDatePicker = new DatePicker(LocalDate.now().plusMonths(6)); // Default to 6 months from now
        TextArea instructionsArea = new TextArea();
        instructionsArea.setWrapText(true);
        instructionsArea.setPromptText("Enter special instructions for the patient...");
        instructionsArea.setPrefRowCount(4);

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        int row = 0;
        grid.add(new Label("Patient:"), 0, row);
        grid.add(new Label(patient.getFullName()), 1, row++);

        grid.add(new Label("Medications:"), 0, row);
        grid.add(medsTable, 0, row, 2, 1); // Span 2 columns
        row++;
        grid.add(medButtons, 0, row++, 2, 1); // Span 2 columns

        grid.add(new Label("Expiry Date:"), 0, row);
        grid.add(expiryDatePicker, 1, row++);

        grid.add(new Label("Instructions:"), 0, row);
        grid.add(instructionsArea, 0, row++, 2, 1); // Span 2 columns

        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        // Enable/Disable OK button based on whether medications are added
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(medications.emptyProperty());


        setResultConverter(button -> {
            if (button == ButtonType.OK) {
                if (medications.isEmpty()) {
                    new Alert(Alert.AlertType.ERROR, "Please add at least one medication.").showAndWait();
                    return null; // Do not close dialog
                }
                if (expiryDatePicker.getValue() == null || expiryDatePicker.getValue().isBefore(LocalDate.now())) {
                    new Alert(Alert.AlertType.ERROR, "Please select a valid future expiry date.").showAndWait();
                    return null; // Do not close dialog
                }

                return new Prescription(
                        0, // ID will be set by DB
                        patient.getId(),
                        currentProvider.getId(), // Use the actual provider ID
                        LocalDate.now(),
                        expiryDatePicker.getValue(),
                        FXCollections.observableArrayList(medications), // Pass a copy
                        instructionsArea.getText()
                );
            }
            return null;
        });
    }

    private void showAddMedicationDialog() {
        Dialog<Medication> dialog = new Dialog<>();
        dialog.setTitle("Add Medication");
        dialog.initOwner(getDialogPane().getScene().getWindow()); // Set owner for modality
        dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);

        TextField nameField = new TextField();
        nameField.setPromptText("e.g., Amoxicillin");
        TextField dosageField = new TextField();
        dosageField.setPromptText("e.g., 250mg");
        TextField frequencyField = new TextField();
        frequencyField.setPromptText("e.g., Twice daily");
        TextField durationField = new TextField();
        durationField.setPromptText("e.g., 7 days");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Dosage:"), 0, 1);
        grid.add(dosageField, 1, 1);
        grid.add(new Label("Frequency:"), 0, 2);
        grid.add(frequencyField, 1, 2);
        grid.add(new Label("Duration:"), 0, 3);
        grid.add(durationField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        // Enable/Disable OK button based on name field
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(nameField.textProperty().isEmpty());

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                if (nameField.getText().trim().isEmpty()) {
                    new Alert(Alert.AlertType.ERROR, "Medication name is required.").showAndWait();
                    return null;
                }
                return new Medication(
                        nameField.getText().trim(),
                        dosageField.getText().trim(),
                        frequencyField.getText().trim(),
                        durationField.getText().trim()
                );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(med -> medications.add(med));
    }
}
