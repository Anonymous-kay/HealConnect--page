module com.healconnect.healconnect {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.web; // For WebView
    requires javafx.swing; // For SwingFXUtils (PDFBox)
    requires java.desktop; // For BufferedImage, java.awt.Desktop
    requires java.logging; // Often useful for general logging

    requires org.apache.pdfbox; // For PDF handling
    requires org.mindrot.jbcrypt; // For jbcrypt
    requires com.google.gson; // For Gson

    // For TestFX and Testcontainers (if running tests)
    requires org.junit.jupiter.api;
    requires org.testcontainers.junit.jupiter;
    requires org.mockito;
    requires org.testfx.core;
    requires org.testfx.junit5;
    requires org.testcontainers.mysql; // For MySQL container

    // WebRTC (if you manage to integrate it)
    // requires org.webrtc; // This would be the module name if you have a proper desktop WebRTC library

    opens com.healconnect.healconnect to javafx.fxml;
    opens com.healconnect.healconnect.model to javafx.base, com.google.gson; // Allow JavaFX and Gson to access model properties
    opens com.healconnect.healconnect.controller to javafx.fxml;
    opens com.healconnect.healconnect.view to javafx.fxml; // If FXMLs are in view package
    opens com.healconnect.healconnect.telemedicine to javafx.fxml; // Open telemedicine package for FXML

    // Open test packages for TestFX
    opens com.healconnect.ui to javafx.fxml;
    opens com.healconnect.service to org.junit.jupiter.api, org.testcontainers.junit.jupiter, org.mockito;
    opens com.healconnect.telemedicine to org.junit.jupiter.api, org.mockito;


    exports com.healconnect.healconnect;
    exports com.healconnect.healconnect.model;
    exports com.healconnect.healconnect.dao;
    exports com.healconnect.healconnect.controller;
    exports com.healconnect.healconnect.service;
    exports com.healconnect.healconnect.telemedicine;
    exports com.healconnect.healconnect.util;
    exports com.healconnect.healconnect.view;
}
    