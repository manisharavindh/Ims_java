package com.lms.controller;

import com.lms.model.Member;
import com.lms.service.MemberService;
import com.lms.service.LibraryException;
import com.lms.util.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class MemberController {
    
    @FXML private TableView<Member> memberTable;
    @FXML private TextField searchField;
    
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField deptField;
    
    private final MemberService memberService = new MemberService();
    private ObservableList<Member> memberList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        memberTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });
        
        handleRefresh();
    }

    private void populateForm(Member member) {
        nameField.setText(member.getName());
        emailField.setText(member.getEmail());
        phoneField.setText(member.getPhone() != null ? member.getPhone() : "");
        deptField.setText(member.getDepartment());
    }

    private void clearForm() {
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        deptField.clear();
        memberTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleSearch() {
        String keyword = searchField.getText();
        List<Member> results = memberService.searchMembers(keyword);
        memberList.setAll(results);
        memberTable.setItems(memberList);
    }

    @FXML
    public void handleAdd() {
        try {
            Member member = new Member(0, nameField.getText(), emailField.getText(), 
                                       phoneField.getText(), deptField.getText());
            
            memberService.addMember(member);
            AlertHelper.showInfo("Success", "Member added successfully!");
            clearForm();
            handleRefresh();
        } catch (LibraryException e) {
            AlertHelper.showError("Validation Error", e.getMessage());
        }
    }

    @FXML
    public void handleEdit() {
        Member selected = memberTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Error", "Please select a member to edit.");
            return;
        }

        try {
            selected.setName(nameField.getText());
            selected.setEmail(emailField.getText());
            selected.setPhone(phoneField.getText());
            selected.setDepartment(deptField.getText());
            
            memberService.updateMember(selected);
            AlertHelper.showInfo("Success", "Member updated successfully!");
            clearForm();
            handleRefresh();
        } catch (LibraryException e) {
            AlertHelper.showError("Validation Error", e.getMessage());
        }
    }

    @FXML
    public void handleDelete() {
        Member selected = memberTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Error", "Please select a member to delete.");
            return;
        }

        if (AlertHelper.showConfirmation("Delete Member", "Are you sure you want to delete '" + selected.getName() + "'?")) {
            try {
                memberService.deleteMember(selected.getMemberId());
                AlertHelper.showInfo("Success", "Member deleted successfully!");
                clearForm();
                handleRefresh();
            } catch (Exception e) {
                AlertHelper.showError("Error", "Could not delete member. They may be linked to active transactions.");
            }
        }
    }

    @FXML
    public void handleRefresh() {
        memberList.setAll(memberService.getAllMembers());
        memberTable.setItems(memberList);
        searchField.clear();
    }
}
