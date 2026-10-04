package com.lms.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {
    @FXML private Label totalBooksLabel;
    @FXML private Label availableBooksLabel;
    @FXML private Label issuedBooksLabel;
    @FXML private Label totalMembersLabel;

    @FXML
    public void initialize() {
        // TEMPORARY PHASE 6 BEHAVIOR:
        // Hardcoded placeholder statistics
        totalBooksLabel.setText("250");
        availableBooksLabel.setText("180");
        issuedBooksLabel.setText("70");
        totalMembersLabel.setText("120");
    }
}
