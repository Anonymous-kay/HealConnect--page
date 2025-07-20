package com.healconnect.ui;

import org.junit.jupiter.api.*;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.scene.control.*;
import javafx.stage.Stage;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

class LoginScreenTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {
        new HealConnectApplication().start(stage);
    }

    @Test
    void should_contain_login_button() {
        verifyThat("#loginButton", hasText("Sign In"));
    }

    @Test
    void should_show_error_on_invalid_credentials() {
        clickOn("#usernameField").write("invalid");
        clickOn("#passwordField").write("wrongpass");
        clickOn("#loginButton");

        verifyThat("#messageLabel", hasText("Invalid username or password"));
    }

    @AfterEach
    void cleanup() throws Exception {
        FxToolkit.hideStage();
    }
}
