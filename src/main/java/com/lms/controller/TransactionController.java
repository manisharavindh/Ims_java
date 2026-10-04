package com.lms.controller;

import com.lms.model.Book;
import com.lms.model.Member;
import com.lms.model.Transaction;
import com.lms.service.BookService;
import com.lms.service.MemberService;
import com.lms.service.TransactionService;
import com.lms.service.LibraryException;
import com.lms.util.AlertHelper;
import com.lms.util.ViewSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class TransactionController {
    
    // Issue Dialog Fields
    @FXML private ComboBox<String> memberComboBox;
    @FXML private ComboBox<String> bookComboBox;
    @FXML private DatePicker issueDatePicker;
    @FXML private DatePicker dueDatePicker;

    // Return Dialog Fields
    @FXML private TextField transactionIdField;
    @FXML private Label returnInfoLabel;

    private final BookService bookService = new BookService();
    private final MemberService memberService = new MemberService();
    private final TransactionService transactionService = new TransactionService();

    @FXML
    public void initialize() {
        if (memberComboBox != null) {
            loadMembersAndBooks();
            issueDatePicker.setValue(LocalDate.now());
            dueDatePicker.setValue(LocalDate.now().plusDays(14));
        }
    }

    private void loadMembersAndBooks() {
        memberComboBox.getItems().clear();
        for (Member m : memberService.getAllMembers()) {
            memberComboBox.getItems().add(m.getMemberId() + " - " + m.getName());
        }

        bookComboBox.getItems().clear();
        for (Book b : bookService.getAllBooks()) {
            if (b.getAvailableCopies() > 0) {
                bookComboBox.getItems().add(b.getBookId() + " - " + b.getTitle() + " (" + b.getAvailableCopies() + " avail)");
            }
        }
    }

    @FXML
    public void handleIssueBook() {
        String memberSelection = memberComboBox.getValue();
        String bookSelection = bookComboBox.getValue();
        LocalDate issueDate = issueDatePicker.getValue();
        LocalDate dueDate = dueDatePicker.getValue();

        if (memberSelection == null || bookSelection == null) {
            AlertHelper.showError("Validation Error", "Please select both a Member and a Book.");
            return;
        }

        try {
            int memberId = Integer.parseInt(memberSelection.split(" ")[0]);
            int bookId = Integer.parseInt(bookSelection.split(" ")[0]);

            transactionService.issueBook(bookId, memberId, issueDate, dueDate);
            AlertHelper.showInfo("Success", "Book issued successfully!");
            ViewSwitcher.setStatus("Book issued successfully.");
            closeDialog(memberComboBox);
        } catch (LibraryException e) {
            AlertHelper.showError("Issue Error", e.getMessage());
        } catch (Exception e) {
            AlertHelper.showError("Error", "An unexpected error occurred.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSearchTransaction() {
        String idStr = transactionIdField.getText();
        if (idStr == null || idStr.trim().isEmpty()) {
            AlertHelper.showError("Validation Error", "Please enter a Transaction ID.");
            return;
        }

        try {
            int txId = Integer.parseInt(idStr);
            Transaction tx = transactionService.getAllTransactions().stream()
                    .filter(t -> t.getTransactionId() == txId)
                    .findFirst()
                    .orElse(null);

            if (tx == null) {
                returnInfoLabel.setText("Transaction not found.");
            } else {
                Book book = bookService.getBookById(tx.getBookId());
                Member member = memberService.getMemberById(tx.getMemberId());
                String bookTitle = book != null ? book.getTitle() : "Unknown";
                String memberName = member != null ? member.getName() : "Unknown";
                
                returnInfoLabel.setText(
                    String.format("Status: %s\nBook: %s\nMember: %s\nIssue Date: %s\nDue Date: %s",
                                  tx.getStatus(), bookTitle, memberName, tx.getIssueDate(), tx.getDueDate())
                );
            }
        } catch (NumberFormatException e) {
            AlertHelper.showError("Validation Error", "Transaction ID must be a number.");
        }
    }

    @FXML
    public void handleReturnBook() {
        String idStr = transactionIdField.getText();
        if (idStr == null || idStr.trim().isEmpty()) {
            AlertHelper.showError("Validation Error", "Please search for a transaction first.");
            return;
        }

        try {
            int txId = Integer.parseInt(idStr);
            transactionService.returnBook(txId, LocalDate.now());
            AlertHelper.showInfo("Success", "Book returned successfully!");
            ViewSwitcher.setStatus("Book returned successfully.");
            closeDialog(transactionIdField);
        } catch (NumberFormatException e) {
            AlertHelper.showError("Validation Error", "Transaction ID must be a number.");
        } catch (LibraryException e) {
            AlertHelper.showError("Return Error", e.getMessage());
        } catch (Exception e) {
            AlertHelper.showError("Error", "An unexpected error occurred.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCancelIssue() {
        closeDialog(memberComboBox);
    }
    
    @FXML
    public void handleCancelReturn() {
        closeDialog(transactionIdField);
    }

    private void closeDialog(javafx.scene.Node node) {
        if (node != null && node.getScene() != null) {
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        }
    }
}
