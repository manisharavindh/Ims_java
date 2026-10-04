package com.lms.controller;

import com.lms.model.Book;
import com.lms.model.Member;
import com.lms.model.Transaction;
import com.lms.service.BookService;
import com.lms.service.MemberService;
import com.lms.service.TransactionService;
import com.lms.util.ViewSwitcher;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ReportController {
    
    @FXML private TableView<Object> reportTable;

    private final BookService bookService = new BookService();
    private final MemberService memberService = new MemberService();
    private final TransactionService transactionService = new TransactionService();

    @FXML
    public void initialize() {
        showBorrowingHistory(); // Default view
    }

    @FXML
    public void showAllBooks() {
        setupBookColumns();
        List<Book> data = bookService.getAllBooks();
        reportTable.setItems(FXCollections.observableArrayList(data));
        ViewSwitcher.setStatus("Report: All Books (" + data.size() + " records)");
    }

    @FXML
    public void showAvailableBooks() {
        setupBookColumns();
        List<Book> available = bookService.getAllBooks().stream()
                .filter(b -> b.getAvailableCopies() > 0)
                .collect(Collectors.toList());
        reportTable.setItems(FXCollections.observableArrayList(available));
        ViewSwitcher.setStatus("Report: Available Books (" + available.size() + " records)");
    }

    @FXML
    public void showIssuedBooks() {
        setupTransactionColumns();
        List<Transaction> active = transactionService.getActiveTransactions();
        reportTable.setItems(FXCollections.observableArrayList(active));
        ViewSwitcher.setStatus("Report: Issued Books (" + active.size() + " records)");
    }

    @FXML
    public void showOverdueBooks() {
        setupTransactionColumns();
        List<Transaction> overdue = transactionService.getOverdueTransactions();
        reportTable.setItems(FXCollections.observableArrayList(overdue));
        ViewSwitcher.setStatus("Report: Overdue Books (" + overdue.size() + " records)");
    }

    @FXML
    public void showAllMembers() {
        setupMemberColumns();
        List<Member> members = memberService.getAllMembers();
        reportTable.setItems(FXCollections.observableArrayList(members));
        ViewSwitcher.setStatus("Report: All Members (" + members.size() + " records)");
    }

    @FXML
    public void showBorrowingHistory() {
        setupTransactionColumns();
        List<Transaction> history = transactionService.getAllTransactions();
        reportTable.setItems(FXCollections.observableArrayList(history));
        ViewSwitcher.setStatus("Report: Borrowing History (" + history.size() + " records)");
    }

    private void setupBookColumns() {
        reportTable.getColumns().clear();
        reportTable.getColumns().add(createCol("ID", "bookId", 50));
        reportTable.getColumns().add(createCol("Title", "title", 200));
        reportTable.getColumns().add(createCol("Author", "author", 150));
        reportTable.getColumns().add(createCol("Category", "category", 120));
        reportTable.getColumns().add(createCol("ISBN", "isbn", 120));
        reportTable.getColumns().add(createCol("Total", "totalCopies", 80));
        reportTable.getColumns().add(createCol("Available", "availableCopies", 80));
    }

    private void setupMemberColumns() {
        reportTable.getColumns().clear();
        reportTable.getColumns().add(createCol("ID", "memberId", 50));
        reportTable.getColumns().add(createCol("Name", "name", 200));
        reportTable.getColumns().add(createCol("Email", "email", 200));
        reportTable.getColumns().add(createCol("Phone", "phone", 150));
        reportTable.getColumns().add(createCol("Department", "department", 150));
    }

    private void setupTransactionColumns() {
        reportTable.getColumns().clear();
        reportTable.getColumns().add(createCol("Tx ID", "transactionId", 50));
        reportTable.getColumns().add(createCol("Book ID", "bookId", 60));
        reportTable.getColumns().add(createCol("Member ID", "memberId", 70));
        reportTable.getColumns().add(createCol("Issue Date", "issueDate", 120));
        reportTable.getColumns().add(createCol("Due Date", "dueDate", 120));
        reportTable.getColumns().add(createCol("Return Date", "returnDate", 120));
        reportTable.getColumns().add(createCol("Status", "status", 100));
    }

    private TableColumn<Object, Object> createCol(String title, String property, int width) {
        TableColumn<Object, Object> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setPrefWidth(width);
        return col;
    }
}
