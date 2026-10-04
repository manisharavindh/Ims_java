package com.lms.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.time.LocalDate;

public class TransactionController {
    
    // Issue Book Fields
    @FXML private ComboBox<String> memberComboBox;
    @FXML private ComboBox<String> bookComboBox;
    @FXML private DatePicker issueDatePicker;
    @FXML private DatePicker dueDatePicker;
    @FXML private Label issueMessageLabel;

    // Return Book Fields
    @FXML private TextField transactionIdField;
    @FXML private Label returnInfoLabel;
    @FXML private Label returnMessageLabel;

    @FXML
    public void initialize() {
        if (memberComboBox != null) {
            // TEMPORARY PHASE 6 BEHAVIOR (Issue Book Setup)
            memberComboBox.getItems().addAll("1 - Alice Smith", "2 - Bob Jones");
            bookComboBox.getItems().addAll("1 - Clean Code", "2 - Design Patterns");
            issueDatePicker.setValue(LocalDate.now());
            dueDatePicker.setValue(LocalDate.now().plusDays(14));
        }
    }

    @FXML
    public void handleIssueBook() {
        System.out.println("Issuing book...");
        if (issueMessageLabel != null) {
            issueMessageLabel.setText("Book issued successfully! (Placeholder)");
            issueMessageLabel.setStyle("-fx-text-fill: green;");
        }
    }

    @FXML
    public void handleSearchTransaction() {
        System.out.println("Searching transaction " + transactionIdField.getText());
        if (returnInfoLabel != null) {
            returnInfoLabel.setText("Found: Clean Code (Issued to Alice Smith)");
        }
    }

    @FXML
    public void handleReturnBook() {
        System.out.println("Returning book...");
        if (returnMessageLabel != null) {
            returnMessageLabel.setText("Book returned successfully! (Placeholder)");
            returnMessageLabel.setStyle("-fx-text-fill: green;");
        }
    }
}
