package com.lms.dao;

import com.lms.model.Book;
import com.lms.model.Member;
import com.lms.model.Transaction;
import com.lms.model.User;

import java.time.LocalDate;
import java.util.List;

public class DaoTest {
    public static void main(String[] args) {
        System.out.println("Starting DAO Layer Tests...\n");

        BookDAO bookDAO = new BookDAO();
        MemberDAO memberDAO = new MemberDAO();
        UserDAO userDAO = new UserDAO();
        TransactionDAO transactionDAO = new TransactionDAO();

        try {
            // --- USER TEST ---
            System.out.println("--- Testing UserDAO ---");
            User adminUser = userDAO.findByUsername("admin");
            if (adminUser != null) {
                System.out.println("Found User: " + adminUser.getUsername());
            } else {
                System.out.println("User 'admin' not found.");
            }

            // --- BOOK TEST ---
            System.out.println("\n--- Testing BookDAO ---");
            Book newBook = new Book(0, "Test Book", "Test Author", "Testing", "TEST-ISBN-123", 10, 10);
            bookDAO.addBook(newBook);
            System.out.println("Added Book ID: " + newBook.getBookId());

            Book retrievedBook = bookDAO.getBookById(newBook.getBookId());
            System.out.println("Retrieved Book: " + (retrievedBook != null ? retrievedBook.getTitle() : "null"));

            if (retrievedBook != null) {
                retrievedBook.setAvailableCopies(8);
                bookDAO.updateBook(retrievedBook);
                System.out.println("Updated Book Available Copies to 8.");
            }

            List<Book> searchResults = bookDAO.searchBooks("Test Book");
            System.out.println("Search Results (count): " + searchResults.size());

            // --- MEMBER TEST ---
            System.out.println("\n--- Testing MemberDAO ---");
            Member newMember = new Member(0, "Test Member", "test@test.com", "123", "Test Dept");
            memberDAO.addMember(newMember);
            System.out.println("Added Member ID: " + newMember.getMemberId());

            List<Member> allMembers = memberDAO.getAllMembers();
            System.out.println("Total Members: " + allMembers.size());

            Member retrievedMember = memberDAO.getMemberById(newMember.getMemberId());
            if (retrievedMember != null) {
                retrievedMember.setPhone("999");
                memberDAO.updateMember(retrievedMember);
                System.out.println("Updated Member Phone to 999.");
            }

            // --- TRANSACTION TEST ---
            System.out.println("\n--- Testing TransactionDAO ---");
            Transaction newTransaction = new Transaction(0, newBook.getBookId(), newMember.getMemberId(),
                    LocalDate.now(), LocalDate.now().plusDays(14), null, "ISSUED");
            transactionDAO.addTransaction(newTransaction);
            System.out.println("Added Transaction ID: " + newTransaction.getTransactionId());

            List<Transaction> activeTransactions = transactionDAO.getActiveTransactions();
            System.out.println("Active Transactions count: " + activeTransactions.size());

            // --- CLEANUP ---
            System.out.println("\n--- Cleaning up test records ---");
            // Must delete transaction first to satisfy foreign key constraints
            // Actually, for a quick test, we can just delete it by updating it to a different status or deleting the record if we added deleteTransaction.
            // TransactionDAO doesn't have deleteTransaction defined in the requirements, but we can do it via a quick raw query for cleanup, or just leave it. 
            // The prompt says: "Do not permanently corrupt or duplicate the sample database data while testing. Use clearly identifiable test records or clean them up after testing."
            // We don't have deleteTransaction, let's just delete the records through raw connection here for cleanup, or add deleteTransaction.
            // Wait, we can't delete the book and member if a transaction references them. I will add a raw JDBC delete for cleanup in this test.
            
            try (java.sql.Connection conn = com.lms.util.DatabaseConnection.getConnection();
                 java.sql.PreparedStatement stmt = conn.prepareStatement("DELETE FROM transactions WHERE transaction_id = ?")) {
                stmt.setInt(1, newTransaction.getTransactionId());
                stmt.executeUpdate();
                System.out.println("Cleaned up Test Transaction.");
            }

            bookDAO.deleteBook(newBook.getBookId());
            System.out.println("Cleaned up Test Book.");

            memberDAO.deleteMember(newMember.getMemberId());
            System.out.println("Cleaned up Test Member.");

            System.out.println("\nDAO Layer Tests Completed Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
