package com.lms.controller;

import com.lms.model.Book;
import com.lms.service.BookService;
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

public class BookController {

    @FXML private TableView<Book> bookTable;
    @FXML private TextField searchField;

    // Inline Form Fields
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField categoryField;
    @FXML private TextField isbnField;
    @FXML private TextField totalCopiesField;

    private final BookService bookService = new BookService();
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private Book editingBook = null;

    @FXML
    public void initialize() {
        setupTable();
        
        bookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            } else {
                clearForm();
            }
        });

        bookTable.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
                handleDelete();
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch();
        });

        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN) {
                bookTable.requestFocus();
                if (bookTable.getSelectionModel().isEmpty() && !bookTable.getItems().isEmpty()) {
                    bookTable.getSelectionModel().selectFirst();
                }
            }
        });

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
        
        com.lms.util.UIHelper.addSNoColumn(bookTable);
    }

    private void populateForm(Book book) {
        editingBook = book;
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        categoryField.setText(book.getCategory());
        isbnField.setText(book.getIsbn());
        totalCopiesField.setText(String.valueOf(book.getTotalCopies()));
    }

    @FXML
    public void clearForm() {
        editingBook = null;
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
        bookList.setAll(bookService.searchBooks(keyword));
        bookTable.setItems(bookList);
    }

    @FXML
    public void handleRefresh() {
        searchField.clear();
        bookList.setAll(bookService.getAllBooks());
        bookTable.setItems(bookList);
        clearForm();
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
            handleRefresh();
        } catch (NumberFormatException e) {
            AlertHelper.showError("Validation Error", "Total copies must be a valid number.");
        } catch (LibraryException e) {
            AlertHelper.showError("Validation Error", e.getMessage());
        }
    }

    @FXML
    public void handleDelete() {
        if (editingBook == null) {
            AlertHelper.showError("Selection Required", "Please select a book to delete.");
            return;
        }

        if (AlertHelper.showConfirmation("Delete Book", "Are you sure you want to delete '" + editingBook.getTitle() + "'?")) {
            try {
                bookService.deleteBook(editingBook.getBookId());
                handleRefresh();
            } catch (Exception e) {
                AlertHelper.showError("Error", "Could not delete book. It may be linked to active transactions.");
            }
        }
    }
}
