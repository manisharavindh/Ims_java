package com.lms.controller;

import com.lms.model.Book;
import com.lms.model.Transaction;
import com.lms.service.BookService;
import com.lms.service.MemberService;
import com.lms.service.TransactionService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardController {
    @FXML private Label totalBooksLabel;
    @FXML private Label availableBooksLabel;
    @FXML private Label issuedBooksLabel;
    @FXML private Label totalMembersLabel;
    @FXML private TableView<Transaction> recentTable;

    private final BookService bookService = new BookService();
    private final MemberService memberService = new MemberService();
    private final TransactionService transactionService = new TransactionService();

    @FXML
    public void initialize() {
        setupTable();
        loadStatistics();
    }

    private void setupTable() {
        @SuppressWarnings("unchecked")
        TableColumn<Transaction, ?>[] cols = (TableColumn<Transaction, ?>[]) recentTable.getColumns().toArray(new TableColumn[0]);
        cols[0].setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        cols[1].setCellValueFactory(new PropertyValueFactory<>("bookId"));
        cols[2].setCellValueFactory(new PropertyValueFactory<>("memberId"));
        cols[3].setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        cols[4].setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        cols[5].setCellValueFactory(new PropertyValueFactory<>("status"));
        
        com.lms.util.UIHelper.addSNoColumn(recentTable);
    }

    private void loadStatistics() {
        try {
            int totalBooks = 0;
            int availableBooks = 0;
            for (Book b : bookService.getAllBooks()) {
                totalBooks += b.getTotalCopies();
                availableBooks += b.getAvailableCopies();
            }
            List<Transaction> activeTx = transactionService.getActiveTransactions();
            int issuedBooks = activeTx.size();
            int totalMembers = memberService.getAllMembers().size();

            totalBooksLabel.setText(String.valueOf(totalBooks));
            availableBooksLabel.setText(String.valueOf(availableBooks));
            issuedBooksLabel.setText(String.valueOf(issuedBooks));
            totalMembersLabel.setText(String.valueOf(totalMembers));

            // Load last 10 transactions
            List<Transaction> allTx = transactionService.getAllTransactions();
            List<Transaction> recent = allTx.stream()
                .sorted((t1, t2) -> Integer.compare(t2.getTransactionId(), t1.getTransactionId()))
                .limit(10)
                .collect(Collectors.toList());
            
            recentTable.setItems(FXCollections.observableArrayList(recent));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
