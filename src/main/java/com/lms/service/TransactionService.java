package com.lms.service;

import com.lms.dao.BookDAO;
import com.lms.dao.MemberDAO;
import com.lms.dao.TransactionDAO;
import com.lms.model.Book;
import com.lms.model.Member;
import com.lms.model.Transaction;
import com.lms.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TransactionService {
    private final TransactionDAO transactionDAO;
    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;

    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
        this.bookDAO = new BookDAO();
        this.memberDAO = new MemberDAO();
    }

    public void issueBook(int bookId, int memberId, LocalDate issueDate, LocalDate dueDate) throws LibraryException {
        if (issueDate == null || dueDate == null) {
            throw new LibraryException("Issue date and due date must be provided.");
        }
        if (dueDate.isBefore(issueDate)) {
            throw new LibraryException("Due date cannot be before issue date.");
        }

        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            throw new LibraryException("Book with ID " + bookId + " does not exist.");
        }

        Member member = memberDAO.getMemberById(memberId);
        if (member == null) {
            throw new LibraryException("Member with ID " + memberId + " does not exist.");
        }

        if (book.getAvailableCopies() <= 0) {
            throw new LibraryException("No available copies of '" + book.getTitle() + "' to issue.");
        }

        Transaction transaction = new Transaction();
        transaction.setBookId(bookId);
        transaction.setMemberId(memberId);
        transaction.setIssueDate(issueDate);
        transaction.setDueDate(dueDate);
        transaction.setStatus("ISSUED");

        book.setAvailableCopies(book.getAvailableCopies() - 1);

        // Transactional execution
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            bookDAO.updateBook(conn, book);
            transactionDAO.addTransaction(conn, transaction);

            conn.commit(); // End transaction
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new LibraryException("Database error occurred while issuing the book: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void returnBook(int transactionId, LocalDate returnDate) throws LibraryException {
        if (returnDate == null) {
            throw new LibraryException("Return date must be provided.");
        }

        Transaction transaction = transactionDAO.getTransactionById(transactionId);
        if (transaction == null) {
            throw new LibraryException("Transaction with ID " + transactionId + " does not exist.");
        }

        if (!"ISSUED".equals(transaction.getStatus())) {
            throw new LibraryException("Transaction is already marked as " + transaction.getStatus() + ".");
        }

        if (returnDate.isBefore(transaction.getIssueDate())) {
            throw new LibraryException("Return date cannot be before issue date.");
        }

        Book book = bookDAO.getBookById(transaction.getBookId());
        if (book == null) {
            throw new LibraryException("Associated book not found in the database.");
        }

        transaction.setReturnDate(returnDate);
        transaction.setStatus("RETURNED");
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        // Transactional execution
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            bookDAO.updateBook(conn, book);
            transactionDAO.updateTransaction(conn, transaction);

            conn.commit(); // End transaction
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new LibraryException("Database error occurred while returning the book: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Transaction> getAllTransactions() {
        return transactionDAO.getAllTransactions();
    }

    public List<Transaction> getActiveTransactions() {
        return transactionDAO.getActiveTransactions();
    }

    public List<Transaction> getOverdueTransactions() {
        return transactionDAO.getOverdueTransactions();
    }
}
