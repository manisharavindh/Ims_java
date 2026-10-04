package com.lms.controller;

import com.lms.model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.time.LocalDate;

public class ReportController {
    
    @FXML private TableView<Transaction> reportTable;
    private ObservableList<Transaction> transactionList;

    @FXML
    public void initialize() {
        // TEMPORARY PHASE 6 BEHAVIOR
        transactionList = FXCollections.observableArrayList(
            new Transaction(1, 1, 1, LocalDate.now().minusDays(5), LocalDate.now().plusDays(9), null, "ISSUED"),
            new Transaction(2, 2, 2, LocalDate.now().minusDays(20), LocalDate.now().minusDays(6), LocalDate.now(), "RETURNED")
        );
        reportTable.setItems(transactionList);
    }

    @FXML
    public void showAllBooks() {
        System.out.println("Generating All Books report...");
    }

    @FXML
    public void showAvailableBooks() {
        System.out.println("Generating Available Books report...");
    }

    @FXML
    public void showIssuedBooks() {
        System.out.println("Generating Issued Books report...");
    }

    @FXML
    public void showOverdueBooks() {
        System.out.println("Generating Overdue Books report...");
    }

    @FXML
    public void showAllMembers() {
        System.out.println("Generating All Members report...");
    }
}
