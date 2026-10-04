package com.lms.controller;

import com.lms.model.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MemberController {
    
    @FXML private TableView<Member> memberTable;
    @FXML private TextField searchField;
    
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField deptField;
    
    private ObservableList<Member> memberList;

    @FXML
    public void initialize() {
        // TEMPORARY PHASE 6 BEHAVIOR
        memberList = FXCollections.observableArrayList(
            new Member(1, "Alice Smith", "alice@example.com", "555-0101", "Computer Science"),
            new Member(2, "Bob Jones", "bob@example.com", "555-0102", "Mathematics")
        );
        memberTable.setItems(memberList);
    }

    @FXML
    public void handleSearch() {
        System.out.println("Search clicked: " + searchField.getText());
    }

    @FXML
    public void handleAdd() {
        System.out.println("Add member clicked");
    }

    @FXML
    public void handleEdit() {
        System.out.println("Edit member clicked");
    }

    @FXML
    public void handleDelete() {
        System.out.println("Delete member clicked");
    }

    @FXML
    public void handleRefresh() {
        System.out.println("Refresh clicked");
    }
}
