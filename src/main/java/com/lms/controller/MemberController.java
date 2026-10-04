package com.lms.controller;

import com.lms.model.Member;
import com.lms.service.MemberService;
import com.lms.service.LibraryException;
import com.lms.util.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

public class MemberController {

    @FXML private TableView<Member> memberTable;
    @FXML private TextField searchField;

    // Inline Form Fields
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField deptField;

    private final MemberService memberService = new MemberService();
    private ObservableList<Member> memberList = FXCollections.observableArrayList();
    private Member editingMember = null;

    @FXML
    public void initialize() {
        setupTable();
        
        memberTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            } else {
                clearForm();
            }
        });

        memberTable.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
                handleDelete();
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch();
        });

        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN) {
                memberTable.requestFocus();
                if (memberTable.getSelectionModel().isEmpty() && !memberTable.getItems().isEmpty()) {
                    memberTable.getSelectionModel().selectFirst();
                }
            }
        });

        handleRefresh();
    }

    private void setupTable() {
        @SuppressWarnings("unchecked")
        TableColumn<Member, ?>[] cols = (TableColumn<Member, ?>[]) memberTable.getColumns().toArray(new TableColumn[0]);
        cols[0].setCellValueFactory(new PropertyValueFactory<>("memberId"));
        cols[1].setCellValueFactory(new PropertyValueFactory<>("name"));
        cols[2].setCellValueFactory(new PropertyValueFactory<>("email"));
        cols[3].setCellValueFactory(new PropertyValueFactory<>("phone"));
        cols[4].setCellValueFactory(new PropertyValueFactory<>("department"));
        
        com.lms.util.UIHelper.addSNoColumn(memberTable);
    }

    private void populateForm(Member member) {
        editingMember = member;
        nameField.setText(member.getName());
        emailField.setText(member.getEmail());
        phoneField.setText(member.getPhone());
        deptField.setText(member.getDepartment());
    }

    @FXML
    public void clearForm() {
        editingMember = null;
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        deptField.clear();
        memberTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleSearch() {
        String keyword = searchField.getText();
        memberList.setAll(memberService.searchMembers(keyword));
        memberTable.setItems(memberList);
    }

    @FXML
    public void handleRefresh() {
        searchField.clear();
        memberList.setAll(memberService.getAllMembers());
        memberTable.setItems(memberList);
        clearForm();
    }

    @FXML
    public void handleSave() {
        try {
            if (editingMember == null) {
                Member member = new Member(0, nameField.getText(), emailField.getText(), 
                                           phoneField.getText(), deptField.getText());
                memberService.addMember(member);
            } else {
                editingMember.setName(nameField.getText());
                editingMember.setEmail(emailField.getText());
                editingMember.setPhone(phoneField.getText());
                editingMember.setDepartment(deptField.getText());
                memberService.updateMember(editingMember);
            }
            handleRefresh();
        } catch (LibraryException e) {
            AlertHelper.showError("Validation Error", e.getMessage());
        }
    }

    @FXML
    public void handleDelete() {
        if (editingMember == null) {
            AlertHelper.showError("Selection Required", "Please select a member to delete.");
            return;
        }

        if (AlertHelper.showConfirmation("Delete Member", "Are you sure you want to delete '" + editingMember.getName() + "'?")) {
            try {
                memberService.deleteMember(editingMember.getMemberId());
                handleRefresh();
            } catch (Exception e) {
                AlertHelper.showError("Error", "Could not delete member. They may be linked to active transactions.");
            }
        }
    }
}
