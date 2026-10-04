package com.lms.controller;

import com.lms.Main;
import javafx.fxml.FXML;

public class SidebarController {

    @FXML
    public void goDashboard() {
        Main.switchScene("/fxml/dashboard.fxml");
    }

    @FXML
    public void goBooks() {
        Main.switchScene("/fxml/books.fxml");
    }

    @FXML
    public void goMembers() {
        Main.switchScene("/fxml/members.fxml");
    }

    @FXML
    public void goIssueBook() {
        Main.switchScene("/fxml/issue-book.fxml");
    }

    @FXML
    public void goReturnBook() {
        Main.switchScene("/fxml/return-book.fxml");
    }

    @FXML
    public void goReports() {
        Main.switchScene("/fxml/reports.fxml");
    }

    @FXML
    public void logout() {
        Main.switchScene("/fxml/login.fxml");
    }
}
