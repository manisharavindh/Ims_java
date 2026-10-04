package com.lms.controller;

import com.lms.model.Book;
import com.lms.service.BookService;
import com.lms.service.MemberService;
import com.lms.service.TransactionService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {
    @FXML private Label totalBooksLabel;
    @FXML private Label availableBooksLabel;
    @FXML private Label issuedBooksLabel;
    @FXML private Label totalMembersLabel;

    private final BookService bookService = new BookService();
    private final MemberService memberService = new MemberService();
    private final TransactionService transactionService = new TransactionService();

    @FXML
    public void initialize() {
        loadStatistics();
    }

    private void loadStatistics() {
        try {
            int totalBooks = 0;
            int availableBooks = 0;
            for (Book b : bookService.getAllBooks()) {
                totalBooks += b.getTotalCopies();
                availableBooks += b.getAvailableCopies();
            }
            int issuedBooks = transactionService.getActiveTransactions().size();
            int totalMembers = memberService.getAllMembers().size();

            totalBooksLabel.setText(String.valueOf(totalBooks));
            availableBooksLabel.setText(String.valueOf(availableBooks));
            issuedBooksLabel.setText(String.valueOf(issuedBooks));
            totalMembersLabel.setText(String.valueOf(totalMembers));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
