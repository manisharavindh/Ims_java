package com.lms.controller;

import com.lms.model.Book;
import com.lms.service.BookService;
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

public class BookController {

    @FXML private TableView<Book> bookTable;
    @FXML private TextField searchField;

    private final BookService bookService = new BookService();
    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTable();
        setupInteractions();
        handleRefresh();
    }

    private void setupTable() {
        @SuppressWarnings("unchecked")
        TableColumn<Book, ?>[] cols = (TableColumn<Book, ?>[]) bookTable.getColumns().toArray(new TableColumn[0]);
        cols[0].setCellValueFactory(new PropertyValueFactory<>("bookId"));
        cols[1].setCellValueFactory(new PropertyValueFactory<>("title"));
        cols[2].setCellValueFactory(new PropertyValueFactory<>("author"));
        cols[3].setCellValueFactory(new PropertyValueFactory<>("category"));
        cols[4].setCellValueFactory(new PropertyValueFactory<>("isbn"));
        cols[5].setCellValueFactory(new PropertyValueFactory<>("totalCopies"));
        cols[6].setCellValueFactory(new PropertyValueFactory<>("availableCopies"));
    }

    private void setupInteractions() {
        // Context Menu
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit Book");
        editItem.setOnAction(e -> handleEdit());
        MenuItem deleteItem = new MenuItem("Delete Book");
        deleteItem.setOnAction(e -> handleDelete());
        contextMenu.getItems().addAll(editItem, deleteItem);
        bookTable.setContextMenu(contextMenu);

        // Double-click to Edit
        bookTable.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                handleEdit();
            }
        });

        // Delete key
        bookTable.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
                handleDelete();
            }
        });

        // Search on enter
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSearch();
            }
        });
    }

    @FXML
    public void handleSearch() {
        String keyword = searchField.getText();
        bookList.setAll(bookService.searchBooks(keyword));
        bookTable.setItems(bookList);
        ViewSwitcher.setStatus("Showing " + bookList.size() + " books");
    }

    @FXML
    public void handleRefresh() {
        searchField.clear();
        bookList.setAll(bookService.getAllBooks());
        bookTable.setItems(bookList);
        ViewSwitcher.setStatus("Showing " + bookList.size() + " books");
    }

    @FXML
    public void handleAdd() {
        ViewSwitcher.openDialog("/fxml/dialog_book.fxml", "Add Book");
        handleRefresh(); // Refresh table when dialog closes
    }

    @FXML
    public void handleEdit() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection Required", "Please select a book to edit.");
            return;
        }
        
        ViewSwitcher.openDialog("/fxml/dialog_book.fxml", "Edit Book", (BookDialogController controller) -> {
            controller.setEditMode(selected);
        });
        
        handleRefresh();
    }

    @FXML
    public void handleDelete() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Selection Required", "Please select a book to delete.");
            return;
        }

        if (AlertHelper.showConfirmation("Delete Book", "Are you sure you want to delete '" + selected.getTitle() + "'?")) {
            try {
                bookService.deleteBook(selected.getBookId());
                ViewSwitcher.setStatus("Book deleted successfully");
                handleRefresh();
            } catch (Exception e) {
                AlertHelper.showError("Error", "Could not delete book. It may be linked to active transactions.");
            }
        }
    }
}
