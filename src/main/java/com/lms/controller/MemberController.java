package com.lms.controller;

import com.lms.model.Member;
import com.lms.service.MemberService;
import com.lms.util.AlertHelper;
import com.lms.util.ViewSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class MemberController {

    @FXML private TableView<Member> memberTable;
    @FXML private TextField searchField;

    private final MemberService memberService = new MemberService();
    private ObservableList<Member> memberList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTable();
        setupInteractions();
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
    }

    private void setupInteractions() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit Member");
        editItem.setOnAction(e -> handleEdit());
        MenuItem deleteItem = new MenuItem("Delete Member");
        deleteItem.setOnAction(e -> handleDelete());
        contextMenu.getItems().addAll(editItem, deleteItem);
        memberTable.setContextMenu(contextMenu);

        memberTable.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                handleEdit();
            }
        });

        memberTable.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
                handleDelete();
            }
        });

        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSearch();
            }
        });
    }

    @FXML
    public void handleSearch() {
        String keyword = searchField.getText();
        memberList.setAll(memberService.searchMembers(keyword));
        memberTable.setItems(memberList);
        ViewSwitcher.setStatus("Showing " + memberList.size() + " members");
    }

    @FXML
    public void handleRefresh() {
        searchField.clear();
        memberList.setAll(memberService.getAllMembers());
        memberTable.setItems(memberList);
        ViewSwitcher.setStatus("Showing " + memberList.size() + " members");
    }

    @FXML
    public void handleAdd() {
        ViewSwitcher.openDialog("/fxml/dialog_member.fxml", "Add Member");
        handleRefresh();
    }

    @FXML
    public void handleEdit() {
        Member selected = memberTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection Required", "Please select a member to edit.");
            return;
        }
        
        ViewSwitcher.openDialog("/fxml/dialog_member.fxml", "Edit Member", (MemberDialogController controller) -> {
            controller.setEditMode(selected);
        });
        
        handleRefresh();
    }

    @FXML
    public void handleDelete() {
        Member selected = memberTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection Required", "Please select a member to delete.");
            return;
        }

        if (AlertHelper.showConfirmation("Delete Member", "Are you sure you want to delete '" + selected.getName() + "'?")) {
            try {
                memberService.deleteMember(selected.getMemberId());
                ViewSwitcher.setStatus("Member deleted successfully");
                handleRefresh();
            } catch (Exception e) {
                AlertHelper.showError("Error", "Could not delete member. They may be linked to active transactions.");
            }
        }
    }
}
