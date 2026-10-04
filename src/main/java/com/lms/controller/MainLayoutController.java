package com.lms.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

public class MainLayoutController {

    @FXML private StackPane centerPane;
    @FXML private ToggleGroup tabGroup;
    @FXML private ToggleButton dashBtn, booksBtn, membersBtn, transBtn, reportsBtn, settingsBtn;

    @FXML
    public void initialize() {
        // Handle native JavaFX ToggleGroup selection (fixes arrow keys without CMD)
        tabGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null && oldToggle != null) {
                ((ToggleButton) oldToggle).setSelected(true);
            } else if (newToggle != null) {
                if (newToggle == dashBtn) loadView("/fxml/dashboard.fxml");
                else if (newToggle == booksBtn) loadView("/fxml/books.fxml");
                else if (newToggle == membersBtn) loadView("/fxml/members.fxml");
                else if (newToggle == transBtn) loadView("/fxml/transactions.fxml");
                else if (newToggle == reportsBtn) loadView("/fxml/reports.fxml");
                else if (newToggle == settingsBtn) loadView("/fxml/settings.fxml");
            }
        });

        // Global Arrow Key Navigation (CMD/CTRL + Arrow)
        centerPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    if (event.isControlDown() || event.isShortcutDown()) {
                        ToggleButton[] tabs = {dashBtn, booksBtn, membersBtn, transBtn, reportsBtn, settingsBtn};
                        int current = -1;
                        for (int i = 0; i < tabs.length; i++) {
                            if (tabs[i].isSelected()) current = i;
                        }
                        
                        if (event.getCode() == KeyCode.RIGHT && current < tabs.length - 1 && current != -1) {
                            tabs[current + 1].setSelected(true);
                            tabs[current + 1].requestFocus();
                        } else if (event.getCode() == KeyCode.LEFT && current > 0) {
                            tabs[current - 1].setSelected(true);
                            tabs[current - 1].requestFocus();
                        }
                    }
                });
            }
        });

        // Initialize dashboard
        if (!dashBtn.isSelected()) {
            dashBtn.setSelected(true);
        } else {
            loadView("/fxml/dashboard.fxml");
        }
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();
            centerPane.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void showDashboard() { if (dashBtn.isSelected()) loadView("/fxml/dashboard.fxml"); else dashBtn.setSelected(true); }
    @FXML public void showBooks() { if (booksBtn.isSelected()) loadView("/fxml/books.fxml"); else booksBtn.setSelected(true); }
    @FXML public void showMembers() { if (membersBtn.isSelected()) loadView("/fxml/members.fxml"); else membersBtn.setSelected(true); }
    @FXML public void showTransactions() { if (transBtn.isSelected()) loadView("/fxml/transactions.fxml"); else transBtn.setSelected(true); }
    @FXML public void showReports() { if (reportsBtn.isSelected()) loadView("/fxml/reports.fxml"); else reportsBtn.setSelected(true); }
    @FXML public void showSettings() { if (settingsBtn.isSelected()) loadView("/fxml/settings.fxml"); else settingsBtn.setSelected(true); }
}
