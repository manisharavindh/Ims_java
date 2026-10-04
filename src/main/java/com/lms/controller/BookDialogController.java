package com.lms.controller;

import com.lms.model.Book;
import com.lms.service.BookService;
import com.lms.service.LibraryException;
import com.lms.util.AlertHelper;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BookDialogController {

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField categoryField;
    @FXML private TextField isbnField;
    @FXML private TextField totalCopiesField;

    private Book editingBook = null;
    private final BookService bookService = new BookService();

    public void setEditMode(Book book) {
        this.editingBook = book;
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        categoryField.setText(book.getCategory());
        isbnField.setText(book.getIsbn());
        totalCopiesField.setText(String.valueOf(book.getTotalCopies()));
    }

    @FXML
    public void handleSave() {
        try {
            int totalCopies = Integer.parseInt(totalCopiesField.getText());

            if (editingBook == null) {
                // Add Mode
                Book book = new Book(0, titleField.getText(), authorField.getText(), 
                                     categoryField.getText(), isbnField.getText(), 
                                     totalCopies, totalCopies);
                bookService.addBook(book);
            } else {
                // Edit Mode
                int copyDiff = totalCopies - editingBook.getTotalCopies();
                int newAvailable = editingBook.getAvailableCopies() + copyDiff;
                
                editingBook.setTitle(titleField.getText());
                editingBook.setAuthor(authorField.getText());
                editingBook.setCategory(categoryField.getText());
                editingBook.setIsbn(isbnField.getText());
                editingBook.setTotalCopies(totalCopies);
                editingBook.setAvailableCopies(newAvailable);
                
                bookService.updateBook(editingBook);
            }
            closeDialog();
        } catch (NumberFormatException e) {
            AlertHelper.showError("Validation Error", "Total copies must be a valid number.");
        } catch (LibraryException e) {
            AlertHelper.showError("Validation Error", e.getMessage());
        }
    }

    @FXML
    public void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}
