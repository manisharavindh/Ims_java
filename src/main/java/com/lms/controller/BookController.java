package com.lms.controller;

import com.lms.model.Book;
import com.lms.service.BookService;
import com.lms.service.LibraryException;
import com.lms.util.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class BookController {
    
    @FXML private TableView<Book> bookTable;
    @FXML private TextField searchField;
    
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField categoryField;
    @FXML private TextField isbnField;
    @FXML private TextField totalCopiesField;
    
    private final BookService bookService = new BookService();
    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Handle selection for editing
        bookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });
        
        handleRefresh();
    }

    private void populateForm(Book book) {
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        categoryField.setText(book.getCategory() != null ? book.getCategory() : "");
        isbnField.setText(book.getIsbn());
        totalCopiesField.setText(String.valueOf(book.getTotalCopies()));
    }

    private void clearForm() {
        titleField.clear();
        authorField.clear();
        categoryField.clear();
        isbnField.clear();
        totalCopiesField.clear();
        bookTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleSearch() {
        String keyword = searchField.getText();
        List<Book> results = bookService.searchBooks(keyword);
        bookList.setAll(results);
        bookTable.setItems(bookList);
    }

    @FXML
    public void handleAdd() {
        try {
            int totalCopies = Integer.parseInt(totalCopiesField.getText());
            Book book = new Book(0, titleField.getText(), authorField.getText(), 
                                 categoryField.getText(), isbnField.getText(), 
                                 totalCopies, totalCopies);
            
            bookService.addBook(book);
            AlertHelper.showInfo("Success", "Book added successfully!");
            clearForm();
            handleRefresh();
        } catch (NumberFormatException e) {
            AlertHelper.showError("Validation Error", "Total copies must be a valid number.");
        } catch (LibraryException e) {
            AlertHelper.showError("Validation Error", e.getMessage());
        }
    }

    @FXML
    public void handleEdit() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Error", "Please select a book to edit.");
            return;
        }

        try {
            int totalCopies = Integer.parseInt(totalCopiesField.getText());
            
            // Adjust available copies relatively if total copies change
            int copyDiff = totalCopies - selected.getTotalCopies();
            int newAvailable = selected.getAvailableCopies() + copyDiff;

            selected.setTitle(titleField.getText());
            selected.setAuthor(authorField.getText());
            selected.setCategory(categoryField.getText());
            selected.setIsbn(isbnField.getText());
            selected.setTotalCopies(totalCopies);
            selected.setAvailableCopies(newAvailable);
            
            bookService.updateBook(selected);
            AlertHelper.showInfo("Success", "Book updated successfully!");
            clearForm();
            handleRefresh();
        } catch (NumberFormatException e) {
            AlertHelper.showError("Validation Error", "Total copies must be a valid number.");
        } catch (LibraryException e) {
            AlertHelper.showError("Validation Error", e.getMessage());
        }
    }

    @FXML
    public void handleDelete() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("Error", "Please select a book to delete.");
            return;
        }

        if (AlertHelper.showConfirmation("Delete Book", "Are you sure you want to delete '" + selected.getTitle() + "'?")) {
            try {
                bookService.deleteBook(selected.getBookId());
                AlertHelper.showInfo("Success", "Book deleted successfully!");
                clearForm();
                handleRefresh();
            } catch (Exception e) {
                AlertHelper.showError("Error", "Could not delete book. It may be linked to active transactions.");
            }
        }
    }

    @FXML
    public void handleRefresh() {
        bookList.setAll(bookService.getAllBooks());
        bookTable.setItems(bookList);
        searchField.clear();
    }
}
