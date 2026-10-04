package com.lms.controller;

import com.lms.Main;
import com.lms.service.AuthService;
import com.lms.service.LibraryException;
import com.lms.util.AlertHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class LoginController {
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        // Pressing Enter logs in
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });
    }

    @FXML
    public void handleLogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();
        errorLabel.setText("");

        try {
            authService.authenticate(user, pass);
            Main.switchScene("/fxml/main_layout.fxml", 1000, 700);
        } catch (LibraryException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Database connection error.");
            e.printStackTrace();
        }
    }
}
