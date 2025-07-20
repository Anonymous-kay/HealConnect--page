package com.healconnect.healconnect.view; // Corrected package

import com.healconnect.healconnect.model.MedicalRecord;
import com.healconnect.healconnect.dao.MedicalRecordDAO; // Corrected import
import com.healconnect.healconnect.util.ValidationUtils; // Corrected import
import javafx.geometry.Insets; // Added import
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage; // Added import

public class EditRecordDialog extends Dialog<Boolean> {
    private final MedicalRecord record;
    private final TextArea descriptionArea;
    private final Label errorLabel;

    public EditRecordDialog(MedicalRecord record) {
        this.record = record;
        setTitle("Edit Medical Record");
        // Set the owner window if available for proper modality behavior
        // initOwner(stage);
        initModality(Modality.APPLICATION_MODAL);
        setResizable(true); // Allow resizing

        // Create header
        Label header = new Label("Edit Record - " + record.getRecordType());
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        // Description area
        descriptionArea = new TextArea(record.getDescription());
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(5); // Set preferred row count
        descriptionArea.setPrefColumnCount(30); // Set preferred column count

        // Error label
        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        // Layout
        VBox layout = new VBox(10, header, descriptionArea, errorLabel);
        layout.setPadding(new Insets(15));
        layout.setPrefWidth(400); // Set preferred width
        layout.setPrefHeight(250); // Set preferred height

        getDialogPane().setContent(layout);
        getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        // Enable/Disable OK button based on validation
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(descriptionArea.textProperty().isEmpty());


        // Handle OK button
        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                if (validateForm()) {
                    MedicalRecordDAO recordDAO = new MedicalRecordDAO();
                    // Update the record object with new description
                    record.setDescription(descriptionArea.getText());
                    // For versioning, you might need to pass the current user ID
                    // For simplicity, let's assume updatedBy is 1 for now.
                    record.setUpdatedBy(1); // TODO: Replace with actual current user ID
                    record.setLastUpdated(java.time.LocalDateTime.now()); // Update timestamp

                    boolean success = recordDAO.updateRecord(record);
                    if (success) {
                        // If update is successful, create a new version
                        // MedicalRecordVersionDAO versionDAO = new MedicalRecordVersionDAO();
                        // versionDAO.createVersion(record, record.getUpdatedBy());
                        return true; // Indicate success
                    } else {
                        errorLabel.setText("Failed to update record in database.");
                        return false;
                    }
                }
            }
            return false; // Indicate failure or cancellation
        });
    }

    private boolean validateForm() {
        boolean isValid = true;
        ValidationUtils.clearError(errorLabel);

        // Validate description
        if (!ValidationUtils.validateRequired(descriptionArea, errorLabel, "Description")) {
            isValid = false;
        }

        return isValid;
    }
}
