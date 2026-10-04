package com.lms.controller;

import com.lms.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;

public class MainLayoutController {

    @FXML
    public void handleLogout() {
        Main.switchScene("/fxml/login.fxml", 400, 350);
    }

    @FXML
    public void handleExit() {
        Platform.exit();
    }
}
