package com.lms.controller;

import com.lms.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class BookController {
    
    @FXML private TableView<Book> bookTable;
    @FXML private TextField searchField;
    
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField categoryField;
    @FXML private TextField isbnField;
    @FXML private TextField totalCopiesField;
    
    private ObservableList<Book> bookList;

    @FXML
    public void initialize() {
        // TEMPORARY PHASE 6 BEHAVIOR: 
        bookList = FXCollections.observableArrayList(
            new Book(1, "Clean Code", "Robert C. Martin", "Programming", "978-0132350884", 5, 5),
            new Book(2, "Design Patterns", "Erich Gamma", "Software Engineering", "978-0201633610", 3, 2)
        );
        // Table columns are mapped in FXML using PropertyValueFactory
        bookTable.setItems(bookList);
    }

    @FXML
    public void handleSearch() {
        System.out.println("Search clicked: " + searchField.getText());
    }

    @FXML
    public void handleAdd() {
        System.out.println("Add book clicked");
    }

    @FXML
    public void handleEdit() {
        System.out.println("Edit book clicked");
    }

    @FXML
    public void handleDelete() {
        System.out.println("Delete book clicked");
    }

    @FXML
    public void handleRefresh() {
        System.out.println("Refresh clicked");
    }
}
