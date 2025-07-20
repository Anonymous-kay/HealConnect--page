// ... (existing imports)
import com.healconnect.healconnect.util.ActionButtonCellFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MedicalRecordsController {
    // ... (existing FXML fields)

    // ... (existing currentPatientId)

    public void initialize(int patientId) {
        this.currentPatientId = patientId;
        setupTable();
        loadRecords();
        setupFilterComboBox();
    }

    // ... (loadRecords and setupTable methods)

    private void setupTable() {
        // ... (existing column definitions)

        // Configure action buttons column
        TableColumn<MedicalRecord, Void> actionsCol = new TableColumn<>("Actions"); // Changed Integer to Void
        actionsCol.setCellFactory(new ActionButtonCellFactory<>(
                this::handleViewRecord,
                this::handleEditRecord,
                this::handleDeleteRecord
        ));
        recordsTableView.getColumns().add(actionsCol);
    }

    // Corrected method signatures
    private void handleViewRecord(MedicalRecord record) {
        RecordDetailDialog dialog = new RecordDetailDialog(record);
        dialog.showAndWait();
    }

    private void handleEditRecord(MedicalRecord record) {
        EditRecordDialog dialog = new EditRecordDialog(record);
        Optional<Boolean> result = dialog.showAndWait();

        if (result.isPresent() && result.get()) {
            loadRecords();
        }
    }

    private void handleDeleteRecord(MedicalRecord record) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete this medical record?");
        confirm.setContentText("This action cannot be undone.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                MedicalRecordDAO recordDAO = new MedicalRecordDAO();
                if (recordDAO.deleteRecord(record.getId())) {
                    loadRecords();
                    System.out.println("Record deleted successfully.");
                } else {
                    System.err.println("Failed to delete record.");
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to delete record.");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    // ... (rest of the class)
}
    