package com.lms.controller;

import com.lms.Main;
import com.lms.service.AuthService;
import com.lms.service.LibraryException;
import com.lms.util.AlertHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SettingsController {

    @FXML private TextField currentUsernameField;
    @FXML private PasswordField currentPasswordField;
    @FXML private TextField newUsernameField;
    @FXML private PasswordField newPasswordField;

    private final AuthService authService = new AuthService();

    @FXML
    public void handleUpdateCredentials() {
        String currentUsername = currentUsernameField.getText();
        String currentPassword = currentPasswordField.getText();
        String newUsername = newUsernameField.getText();
        String newPassword = newPasswordField.getText();

        if (currentUsername.isEmpty() || currentPassword.isEmpty() || newUsername.isEmpty() || newPassword.isEmpty()) {
            AlertHelper.showError("Validation Error", "All fields are required.");
            return;
        }

        try {
            authService.updateCredentials(currentUsername, currentPassword, newUsername, newPassword);
            AlertHelper.showInfo("Success", "Credentials updated successfully.\nPlease use the new credentials on next login.");
            
            currentUsernameField.clear();
            currentPasswordField.clear();
            newUsernameField.clear();
            newPasswordField.clear();
            
        } catch (LibraryException e) {
            AlertHelper.showError("Update Error", e.getMessage());
        }
    }

    @FXML
    public void handleLogout() {
        if (AlertHelper.showConfirmation("Logout", "Are you sure you want to log out?")) {
            Main.switchScene("/fxml/login.fxml", 400, 350);
        }
    }

    @FXML
    public void handleExit() {
        if (AlertHelper.showConfirmation("Exit", "Are you sure you want to exit the application?")) {
            Platform.exit();
        }
    }
}
