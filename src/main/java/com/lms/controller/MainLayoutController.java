package com.lms.controller;

import com.lms.Main;
import com.lms.util.AlertHelper;
import com.lms.util.ViewSwitcher;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MainLayoutController {

    @FXML private StackPane centerPane;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        ViewSwitcher.setMainController(this);
        showDashboard();
    }

    public void loadCenterView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();
            centerPane.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
            setStatus("Error loading view: " + fxmlPath);
        }
    }

    public void setStatus(String message) {
        statusLabel.setText(message);
    }

    // --- Menu & Toolbar Actions ---

    @FXML public void showDashboard() {
        loadCenterView("/fxml/dashboard.fxml");
        setStatus("Viewing Dashboard");
    }

    @FXML public void showBooks() {
        loadCenterView("/fxml/books.fxml");
        setStatus("Viewing Books");
    }

    @FXML public void showMembers() {
        loadCenterView("/fxml/members.fxml");
        setStatus("Viewing Members");
    }

    @FXML public void showReports() {
        loadCenterView("/fxml/reports.fxml");
        setStatus("Viewing Reports");
    }

    @FXML public void handleNewBook() {
        ViewSwitcher.openDialog("/fxml/dialog_book.fxml", "Add Book");
        refreshActiveView();
    }

    @FXML public void handleNewMember() {
        ViewSwitcher.openDialog("/fxml/dialog_member.fxml", "Add Member");
        refreshActiveView();
    }

    @FXML public void handleIssueBook() {
        ViewSwitcher.openDialog("/fxml/dialog_issue.fxml", "Issue Book");
        refreshActiveView();
    }

    @FXML public void handleReturnBook() {
        ViewSwitcher.openDialog("/fxml/dialog_return.fxml", "Return Book");
        refreshActiveView();
    }

    @FXML public void handleLogout() {
        Main.switchScene("/fxml/login.fxml", 400, 350);
    }

    @FXML public void handleExit() {
        Platform.exit();
    }

    @FXML public void handleAbout() {
        AlertHelper.showInfo("About", "Library Management System v1.0\nClassic Desktop Edition");
    }

    /**
     * Re-loads the active view to reflect data changes from dialogs.
     * For a simple project, reloading the FXML is robust and completely resets the state.
     */
    private void refreshActiveView() {
        if (centerPane.getChildren().isEmpty()) return;
        Node current = centerPane.getChildren().get(0);
        // We track the active view loosely via status text or we can just reload based on what's active.
        // A simple brute-force way is just calling initialize on the active controller if we tracked it.
        // But since we just need it to work reliably, we'll re-trigger the view based on a naive check.
        String status = statusLabel.getText();
        if (status.contains("Dashboard")) showDashboard();
        else if (status.contains("Books")) showBooks();
        else if (status.contains("Members")) showMembers();
        else if (status.contains("Reports")) showReports();
    }
}
