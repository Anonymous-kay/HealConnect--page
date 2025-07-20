package com.healconnect.healconnect;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HealConnectApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load the login view instead of hello-view
        FXMLLoader fxmlLoader = new FXMLLoader(HealConnectApplication.class.getResource("/com/healconnect/healconnect/login-view.fxml")); // Assuming login-view.fxml exists
        Scene scene = new Scene(fxmlLoader.load(), 600, 400); // Adjust size as needed
        stage.setTitle("HealConnect - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
    