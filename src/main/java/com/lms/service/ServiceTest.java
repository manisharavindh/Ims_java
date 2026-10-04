package com.lms.service;

import com.lms.model.Book;
import com.lms.model.Member;
import com.lms.model.Transaction;
import com.lms.model.User;

import java.time.LocalDate;
import java.util.List;

public class ServiceTest {
    public static void main(String[] args) {
        System.out.println("Starting Service Layer Tests...\n");

        BookService bookService = new BookService();
        MemberService memberService = new MemberService();
        AuthService authService = new AuthService();
        TransactionService transactionService = new TransactionService();

        try {
            // --- AUTH TEST ---
            System.out.println("--- Testing AuthService ---");
            User user = authService.authenticate("admin", "admin123");
            System.out.println("Login SUCCESS for user: " + user.getUsername());
            
            try {
                authService.authenticate("admin", "wrongpassword");
            } catch (LibraryException e) {
                System.out.println("Login FAILURE caught successfully: " + e.getMessage());
            }

            // --- BOOK TEST ---
            System.out.println("\n--- Testing BookService ---");
            Book newBook = new Book(0, "Service Book", "Author", "IT", "ISBN-999", 2, 2);
            bookService.addBook(newBook);
            System.out.println("Added Book ID: " + newBook.getBookId());

            try {
                bookService.addBook(new Book(0, "", "Author", "IT", "ISBN-000", 1, 1));
            } catch (LibraryException e) {
                System.out.println("Validation caught empty title: " + e.getMessage());
            }

            // --- MEMBER TEST ---
            System.out.println("\n--- Testing MemberService ---");
            Member newMember = new Member(0, "Service Member", "service@example.com", "9876543210", "HR");
            memberService.addMember(newMember);
            System.out.println("Added Member ID: " + newMember.getMemberId());

            try {
                memberService.addMember(new Member(0, "Bad", "bademail", "123", "IT"));
            } catch (LibraryException e) {
                System.out.println("Validation caught invalid email: " + e.getMessage());
            }

            // --- TRANSACTION TEST ---
            System.out.println("\n--- Testing TransactionService ---");
            System.out.println("Issuing book...");
            transactionService.issueBook(newBook.getBookId(), newMember.getMemberId(), LocalDate.now(), LocalDate.now().plusDays(10));
            
            // Re-fetch to check copies
            Book updatedBook = bookService.getBookById(newBook.getBookId());
            System.out.println("Available copies after issue: " + updatedBook.getAvailableCopies());

            // Try to issue again, there is 1 copy left, so it should work. Let's issue to the same member for testing.
            transactionService.issueBook(newBook.getBookId(), newMember.getMemberId(), LocalDate.now(), LocalDate.now().plusDays(10));
            updatedBook = bookService.getBookById(newBook.getBookId());
            System.out.println("Available copies after second issue: " + updatedBook.getAvailableCopies());

            // Try to issue again (0 copies left)
            try {
                transactionService.issueBook(newBook.getBookId(), newMember.getMemberId(), LocalDate.now(), LocalDate.now().plusDays(10));
            } catch (LibraryException e) {
                System.out.println("Validation caught no available copies: " + e.getMessage());
            }

            // Return the book
            List<Transaction> activeTx = transactionService.getActiveTransactions();
            Transaction lastTx = activeTx.get(activeTx.size() - 1);
            
            System.out.println("Returning book...");
            transactionService.returnBook(lastTx.getTransactionId(), LocalDate.now());
            
            updatedBook = bookService.getBookById(newBook.getBookId());
            System.out.println("Available copies after return: " + updatedBook.getAvailableCopies());

            try {
                transactionService.returnBook(lastTx.getTransactionId(), LocalDate.now());
            } catch (LibraryException e) {
                System.out.println("Validation caught already returned: " + e.getMessage());
            }

            System.out.println("\n--- Cleaning up test records ---");
            // Hard deletion for cleanup since Services don't support deleting transactions natively
            try (java.sql.Connection conn = com.lms.util.DatabaseConnection.getConnection();
                 java.sql.PreparedStatement stmt = conn.prepareStatement("DELETE FROM transactions WHERE member_id = ?")) {
                stmt.setInt(1, newMember.getMemberId());
                stmt.executeUpdate();
            }

            bookService.deleteBook(newBook.getBookId());
            memberService.deleteMember(newMember.getMemberId());

            System.out.println("Service Layer Tests Completed Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
