package com.lms.controller;

import com.lms.model.Member;
import com.lms.service.MemberService;
import com.lms.service.LibraryException;
import com.lms.util.AlertHelper;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MemberDialogController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField deptField;

    private Member editingMember = null;
    private final MemberService memberService = new MemberService();

    public void setEditMode(Member member) {
        this.editingMember = member;
        nameField.setText(member.getName());
        emailField.setText(member.getEmail());
        phoneField.setText(member.getPhone());
        deptField.setText(member.getDepartment());
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
            closeDialog();
        } catch (LibraryException e) {
            AlertHelper.showError("Validation Error", e.getMessage());
        }
    }

    @FXML
    public void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
