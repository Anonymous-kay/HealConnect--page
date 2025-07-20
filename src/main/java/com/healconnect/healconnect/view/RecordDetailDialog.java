package com.healconnect.healconnect.view; // Corrected package

import com.healconnect.healconnect.model.MedicalRecord;
import com.healconnect.healconnect.util.AppConfig; // Corrected import
import javafx.geometry.Insets; // Added import
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;

import java.io.IOException;
import java.nio.file.Files; // Added import
import java.nio.file.Paths; // Added import

public class RecordDetailDialog extends Dialog<Void> {
    private final MedicalRecord record;

    public RecordDetailDialog(MedicalRecord record) {
        this.record = record;
        setTitle("Medical Record Details");
        initModality(Modality.APPLICATION_MODAL);
        setResizable(true); // Allow resizing

        // Create header
        Label header = new Label(record.getRecordType() + " - " + record.getRecordDate());
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        // Description area
        TextArea descriptionArea = new TextArea(record.getDescription());
        descriptionArea.setEditable(false);
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(5);
        descriptionArea.setPrefColumnCount(40);

        // Preview container
        StackPane previewContainer = new StackPane();
        previewContainer.setPrefSize(400, 400); // Use prefSize for better layout control
        previewContainer.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 1px;");

        Label previewPlaceholder = new Label("No preview available or no document attached.");
        previewContainer.getChildren().add(previewPlaceholder);

        if (record.hasAttachment()) {
            try {
                // Assuming a dummy user ID for access control for now
                int currentUserId = 1; // TODO: Replace with actual logged-in user ID
                int recordOwnerId = record.getPatientId(); // Assuming patient is the record owner

                FileStorageService fileStorageService = new FileStorageService(AppConfig.getUploadDir());
                byte[] fileData = fileStorageService.loadFile(record.getFilePath(), currentUserId, recordOwnerId);
                Image preview = DocumentPreviewService.generatePreview(fileData, record.getFilePath());

                if (preview != null) {
                    ImageView imageView = new ImageView(preview);
                    imageView.setPreserveRatio(true);
                    imageView.setFitWidth(previewContainer.getPrefWidth() - 20); // Fit within padding
                    imageView.setFitHeight(previewContainer.getPrefHeight() - 20);
                    previewContainer.getChildren().setAll(imageView); // Replace placeholder
                } else {
                    previewContainer.getChildren().setAll(new Label("No preview available for this file type."));
                }
            } catch (SecurityException se) {
                previewContainer.getChildren().setAll(new Label("Access Denied: " + se.getMessage()));
                System.err.println("Access Denied: " + se.getMessage());
            } catch (IOException e) {
                previewContainer.getChildren().setAll(new Label("Error loading preview: " + e.getMessage()));
                System.err.println("Error loading preview: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                previewContainer.getChildren().setAll(new Label("An unexpected error occurred: " + e.getMessage()));
                System.err.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Button to view full document
        Button viewFullButton = new Button("Download Document");
        viewFullButton.setDisable(!record.hasAttachment());

        // Layout
        VBox layout = new VBox(10, header, new Label("Description:"), descriptionArea, new Label("Document Preview:"), previewContainer, viewFullButton);
        layout.setPadding(new Insets(15));
        layout.setPrefWidth(450); // Adjust overall dialog width
        layout.setPrefHeight(650); // Adjust overall dialog height

        getDialogPane().setContent(layout);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        // Handle view full button
        viewFullButton.setOnAction(e -> {
            try {
                // Logic to download or open the full document
                // This can be similar to the download functionality in MedicalRecordsController
                // For simplicity, let's just save it to a temporary location and open it.
                int currentUserId = 1; // TODO: Replace with actual logged-in user ID
                int recordOwnerId = record.getPatientId(); // Assuming patient is the record owner

                FileStorageService fileStorageService = new FileStorageService(AppConfig.getUploadDir());
                byte[] fileData = fileStorageService.loadFile(record.getFilePath(), currentUserId, recordOwnerId);

                // Suggest a file name for download
                String originalFileName = record.getOriginalFileName();
                if (originalFileName.isEmpty()) {
                    originalFileName = "document_" + record.getId() + ".bin"; // Fallback
                }

                // Save to a temporary file and open it
                Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "HealConnectDownloads");
                Files.createDirectories(tempDir); // Ensure directory exists
                Path tempFile = tempDir.resolve(originalFileName);
                Files.write(tempFile, fileData);

                // Open the file with the default system application
                if (java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop.getDesktop().open(tempFile.toFile());
                } else {
                    new Alert(Alert.AlertType.ERROR, "Desktop operations not supported on this system.").showAndWait();
                }

            } catch (SecurityException se) {
                new Alert(Alert.AlertType.ERROR, "Access Denied: " + se.getMessage()).showAndWait();
                System.err.println("Access Denied: " + se.getMessage());
            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR, "Error downloading document: " + ex.getMessage()).showAndWait();
                System.err.println("Error downloading document: " + ex.getMessage());
                ex.printStackTrace();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "An unexpected error occurred during download: " + ex.getMessage()).showAndWait();
                System.err.println("An unexpected error occurred during download: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }
}
