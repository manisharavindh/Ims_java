package com.lms.controller;

import com.lms.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    public void handleLogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (user == null || user.isEmpty() || pass == null || pass.isEmpty()) {
            errorLabel.setText("Please enter username and password");
            return;
        }

        // TEMPORARY PHASE 6 BEHAVIOR:
        // Automatically navigate to dashboard without database authentication.
        // This will be replaced in Phase 7.
        System.out.println("Temporary Login: Navigating to Dashboard...");
        Main.switchScene("/fxml/dashboard.fxml");
    }
}
