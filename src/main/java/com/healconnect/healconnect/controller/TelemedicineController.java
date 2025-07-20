package com.healconnect.healconnect.controller; // Corrected package

import com.healconnect.healconnect.model.Appointment;
import com.healconnect.healconnect.model.Consultation;
import com.healconnect.healconnect.model.Patient;
import com.healconnect.healconnect.model.Prescription;
import com.healconnect.healconnect.telemedicine.AnnotationCanvas;
import com.healconnect.healconnect.telemedicine.CoBrowseHandler;
import com.healconnect.healconnect.view.PrescriptionDialog;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.webrtc.*;

import java.util.Optional;
// import org.webrtc.CameraEnumerator; // This is Android specific
// import org.webrtc.VideoRendererGui; // This is Android specific
// import org.webrtc.ScreenCapturerAndroid; // This is Android specific

public class TelemedicineController {
    // Video Elements
    @FXML private StackPane videoContainer;
    @FXML private ImageView remoteVideoView;
    @FXML private ImageView localVideoView;
    @FXML private Label connectionStatusLabel;

    // Patient Info
    @FXML private ImageView patientAvatar;
    @FXML private Label patientNameLabel;
    @FXML private Label patientAgeGenderLabel;

    // Chat Elements
    @FXML private ListView<String> chatMessagesList;
    @FXML private TextField messageField;

    // Notes
    @FXML private TextArea consultationNotesArea;

    // Collaboration Tools
    @FXML private WebView coBrowserView; // FXML element for co-browsing
    @FXML private AnnotationCanvas annotationCanvas; // FXML element for annotations

    // WebRTC Components
    private PeerConnectionFactory factory;
    private VideoCapturer videoCapturer;
    private VideoSource videoSource;
    private VideoTrack localVideoTrack;
    private PeerConnection peerConnection; // This would be initialized by TelemedicineIntegration

    // Screen Sharing
    private VideoCapturer screenCapturer;
    private VideoSource screenSource;
    private VideoTrack screenTrack;
    private boolean isSharingScreen = false;

    // Co-browsing
    private CoBrowseHandler coBrowseHandler;

    // Current Consultation
    private Consultation currentConsultation;
    private Patient currentPatient; // Store current patient for prescription dialog

    public void initialize(Patient patient, Appointment appointment) {
        this.currentPatient = patient;
        this.currentConsultation = new Consultation(appointment);

        // Load patient info
        loadPatientInfo(patient);

        // Initialize WebRTC (placeholders for desktop)
        initWebRTC();

        // Initialize chat
        initializeChat();

        // Initialize co-browsing handler
        coBrowseHandler = new CoBrowseHandler(coBrowserView);
        // The peerConnection needs to be established before initializing coBrowseHandler with it
        // This would typically happen after signaling completes.
        // For now, assuming peerConnection is available.
        if (peerConnection != null) {
            coBrowseHandler.initialize(peerConnection);
        }

        // Hide collaboration tools initially
        coBrowserView.setVisible(false);
        annotationCanvas.setVisible(false);
    }

    private void loadPatientInfo(Patient patient) {
        patientNameLabel.setText(patient.getFullName());
        patientAgeGenderLabel.setText(patient.getAge() + " • " + patient.getGender());

        // Load avatar (placeholder if none)
        try {
            Image avatar = new Image(patient.getAvatarUrl() != null ?
                    patient.getAvatarUrl() : (String) getClass().getResourceAsStream("/images/default_avatar.png"));
            patientAvatar.setImage(avatar);
        } catch (Exception e) {
            System.err.println("Error loading patient avatar: " + e.getMessage());
            // Set a default image if loading fails
            patientAvatar.setImage(new Image(getClass().getResourceAsStream("/images/default_avatar.png")));
        }
    }

    private void initWebRTC() {
        // For desktop JavaFX, org.webrtc.PeerConnectionFactory.initializeAndroidGlobals() is incorrect.
        // Use PeerConnectionFactory.initialize() and provide a context.
        // This is a simplified placeholder. A real implementation requires careful setup
        // of native libraries or a Java WebRTC wrapper.
        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions.builder(null).createInitializationOptions());
        factory = PeerConnectionFactory.builder().createPeerConnectionFactory();

        // Placeholder for video capturer (desktop camera)
        // This part is highly dependent on native libraries or specific JavaFX camera APIs.
        // The original CameraEnumerator is Android-specific.
        videoCapturer = createDesktopVideoCapturer();
        if (videoCapturer != null) {
            videoSource = factory.createVideoSource(videoCapturer.isScreencast());
            localVideoTrack = factory.createVideoTrack("localVideo", videoSource);

            // Setup local video view (requires a custom VideoRenderer for JavaFX)
            // VideoRendererGui is Android-specific. You'd need to implement a custom JavaFX renderer.
            // For now, just a placeholder.
            // localVideoTrack.addRenderer(new CustomJavaFXVideoRenderer(localVideoView));
            System.out.println("Local video track initialized.");

            // Start capturing (placeholder)
            // videoCapturer.startCapture(1280, 720, 30);
        } else {
            System.err.println("Failed to create video capturer. Local video will not be available.");
        }

        // Initialize peer connection (this would be handled by TelemedicineIntegration)
        initializePeerConnection();
    }

    private VideoCapturer createDesktopVideoCapturer() {
        // This is a placeholder. Real desktop camera access is complex.
        // You might use OpenCV, JavaCV, or other native libraries.
        System.out.println("Placeholder: Creating desktop video capturer.");
        return null; // Return null for now, as actual implementation is missing
    }

    private void initializePeerConnection() {
        // This method would typically set up the PeerConnection object,
        // including ICE servers, and register observers for signaling.
        // The actual peerConnection object would be managed by TelemedicineIntegration.
        System.out.println("Placeholder: Initializing peer connection.");
        // peerConnection = factory.createPeerConnection(...);
    }

    private void initializeChat() {
        chatMessagesList.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String message, boolean empty) {
                super.updateItem(message, empty);
                if (empty || message == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(message);
                    // Apply CSS style for chat bubbles
                    getStyleClass().add("chat-message-bubble");
                }
            }
        });

        // Load previous messages
        loadChatHistory();
    }

    private void loadChatHistory() {
        // TODO: Implementation to load chat history from database or previous session
        System.out.println("Loading chat history...");
        chatMessagesList.getItems().addAll(
                "Patient: Hello Doctor, how are you?",
                "You: I'm good, thank you. How can I help you today?"
        );
    }

    /* Event Handlers */

    @FXML
    private void handleEndCall() {
        // Clean up resources
        if (videoCapturer != null) {
            try {
                videoCapturer.stopCapture();
                videoCapturer.dispose();
            } catch (Exception e) {
                System.err.println("Error stopping video capture: " + e.getMessage());
            }
        }
        if (screenCapturer != null) {
            try {
                screenCapturer.stopCapture();
                screenCapturer.dispose();
            } catch (Exception e) {
                System.err.println("Error stopping screen capture: " + e.getMessage());
            }
        }
        if (localVideoTrack != null) localVideoTrack.dispose();
        if (videoSource != null) videoSource.dispose();
        if (screenTrack != null) screenTrack.dispose();
        if (screenSource != null) screenSource.dispose();
        if (peerConnection != null) peerConnection.close();
        if (factory != null) factory.dispose();

        // Close the consultation
        currentConsultation.endConsultation();

        // Close the window
        Stage stage = (Stage) videoContainer.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    @FXML
    private void handleSendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            // Add to chat UI
            chatMessagesList.getItems().add("You: " + message);

            // Send through signaling server (placeholder)
            sendChatMessage(message);

            // Clear input
            messageField.clear();
        }
    }

    @FXML
    private void handleSaveNotes() {
        currentConsultation.setNotes(consultationNotesArea.getText());
        // TODO: Save to database
        showAlert("Notes Saved", "Consultation notes have been saved.");
    }

    @FXML
    private void handleToggleMute() {
        // TODO: Toggle audio mute
        System.out.println("Toggle Mute clicked.");
        showAlert("Mute/Unmute", "Audio mute toggled (functionality not fully implemented).");
    }

    @FXML
    private void handleToggleVideo() {
        // TODO: Toggle video on/off
        System.out.println("Toggle Video clicked.");
        showAlert("Video On/Off", "Video toggled (functionality not fully implemented).");
    }

    @FXML
    private void handleShareScreen() {
        if (!isSharingScreen) {
            startScreenSharing();
        } else {
            stopScreenSharing();
        }
    }

    @FXML
    private void handleCreatePrescription() {
        PrescriptionDialog dialog = new PrescriptionDialog(currentPatient);
        Optional<Prescription> result = dialog.showAndWait();

        result.ifPresent(prescription -> {
            // Save to database
            boolean success = savePrescription(prescription);

            if (success) {
                // Send to patient (e.g., via notification service or chat)
                sendPrescription(prescription);
                showAlert("Success", "Prescription created and sent to patient.");
            } else {
                showAlert("Error", "Failed to save prescription.");
            }
        });
    }

    @FXML
    private void handleViewRecords() {
        // TODO: Open medical records view for current patient
        System.out.println("View Records clicked.");
        showAlert("View Records", "Medical records view not yet implemented for this context.");
    }

    @FXML
    private void handleStartCoBrowsing() {
        TextInputDialog dialog = new TextInputDialog("https://www.google.com"); // Default URL
        dialog.setTitle("Co-Browsing");
        dialog.setHeaderText("Enter URL to browse together:");
        dialog.setContentText("URL:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(url -> {
            coBrowserView.setVisible(true);
            coBrowserView.getEngine().load(url);
            if (coBrowseHandler != null) {
                coBrowseHandler.sendNavigation(url);
            } else {
                System.err.println("CoBrowseHandler not initialized.");
            }
            showAlert("Co-browsing Started", "Co-browsing session started with: " + url);
        });
    }

    @FXML
    private void handleToggleAnnotation() {
        annotationCanvas.setVisible(!annotationCanvas.isVisible());
        if (annotationCanvas.isVisible()) {
            showAlert("Annotation", "Annotation mode enabled.");
        } else {
            showAlert("Annotation", "Annotation mode disabled.");
        }
        // TODO: Sync annotation state with peer
    }

    @FXML
    private void handleClearAnnotation() {
        annotationCanvas.clearAnnotations();
        showAlert("Annotation", "Annotations cleared.");
        // TODO: Send clear command to peer
    }

    public void startCollaborationSession() {
        coBrowserView.setVisible(true);
        annotationCanvas.setVisible(true);

        // Sync initial state (placeholders)
        syncAnnotations();
        syncWhiteboard();
    }

    private void syncAnnotations() {
        System.out.println("Syncing annotations...");
        // TODO: Implement logic to send/receive current annotation state
    }

    private void syncWhiteboard() {
        System.out.println("Syncing whiteboard...");
        // TODO: Implement logic to send/receive current whiteboard state
    }

    private void sendChatMessage(String message) {
        // TODO: Implement signaling server communication to send chat message
        System.out.println("Sending chat message: " + message);
    }

    private boolean savePrescription(Prescription prescription) {
        // TODO: Implement database save for prescription
        System.out.println("Saving prescription: " + prescription);
        return true; // Assume success for now
    }

    private void sendPrescription(Prescription prescription) {
        // TODO: Implement sending to patient (e.g., via notification service, email, or chat)
        System.out.println("Sending prescription to patient: " + prescription);
    }

    private void startScreenSharing() {
        try {
            // This is a placeholder. Screen capturing on desktop JavaFX is complex
            // and typically requires native libraries (e.g., using Robot class for screenshots
            // and then streaming them, or a dedicated screen capture library).
            // The original ScreenCapturerAndroid is Android-specific.
            screenCapturer = createDesktopScreenCapturer(); // Placeholder method
            if (screenCapturer != null) {
                screenSource = factory.createVideoSource(screenCapturer.isScreencast());
                screenTrack = factory.createVideoTrack("screen", screenSource);

                // Replace remote video with screen share (this logic needs to be adapted for JavaFX)
                // You'd typically add this track to the PeerConnection's sender.
                // For visual feedback, you might display it locally.
                // remoteVideoView.setImage(null); // Clear remote video
                // screenTrack.addRenderer(new CustomJavaFXVideoRenderer(remoteVideoView)); // Render screen share

                // screenCapturer.startCapture(1280, 720, 30); // Start capturing
                isSharingScreen = true;
                showAlert("Screen Sharing", "Screen sharing started.");

                // Notify peer (placeholder)
                sendScreenSharingSignal(true);
            } else {
                showAlert("Screen Sharing Error", "Failed to start screen sharing: Screen capturer not available.");
            }
        } catch (Exception e) {
            showAlert("Screen Sharing Error", "Failed to start screen sharing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private VideoCapturer createDesktopScreenCapturer() {
        // Placeholder for desktop screen capturer.
        // A real implementation would involve capturing screen frames.
        System.out.println("Placeholder: Creating desktop screen capturer.");
        return null; // Return null for now
    }

    private void stopScreenSharing() {
        if (screenCapturer != null) {
            try {
                screenCapturer.stopCapture();
                screenCapturer.dispose();
                if (screenTrack != null) screenTrack.dispose();
                if (screenSource != null) screenSource.dispose();

                // Restore remote video (placeholder)
                setupRemoteVideo();

                isSharingScreen = false;
                showAlert("Screen Sharing", "Screen sharing stopped.");
                sendScreenSharingSignal(false);

            } catch (Exception e) {
                showAlert("Error", "Failed to stop screen sharing: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void setupRemoteVideo() {
        // Reinitialize remote video view (placeholder)
        remoteVideoView.setImage(new Image(getClass().getResourceAsStream("/images/remote_video_placeholder.png")));
        System.out.println("Remote video placeholder restored.");
    }

    private void sendScreenSharingSignal(boolean isSharing) {
        // TODO: Implement signaling to notify peer about screen sharing state
        System.out.println("Sending screen sharing signal: " + isSharing);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
