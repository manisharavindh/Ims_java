package com.lms.service;

import com.lms.dao.BookDAO;
import com.lms.model.Book;

import java.util.List;

public class BookService {
    private final BookDAO bookDAO;

    public BookService() {
        this.bookDAO = new BookDAO();
    }

    public void addBook(Book book) throws LibraryException {
        validateBook(book);
        
        // Check for duplicate ISBN
        for (Book b : bookDAO.getAllBooks()) {
            if (b.getIsbn().equals(book.getIsbn())) {
                throw new LibraryException("A book with ISBN " + book.getIsbn() + " already exists.");
            }
        }

        bookDAO.addBook(book);
    }

    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    public Book getBookById(int bookId) {
        return bookDAO.getBookById(bookId);
    }

    public void updateBook(Book book) throws LibraryException {
        validateBook(book);

        // Check for duplicate ISBN if it changed
        for (Book b : bookDAO.getAllBooks()) {
            if (b.getIsbn().equals(book.getIsbn()) && b.getBookId() != book.getBookId()) {
                throw new LibraryException("Another book with ISBN " + book.getIsbn() + " already exists.");
            }
        }

        bookDAO.updateBook(book);
    }

    public void deleteBook(int bookId) {
        bookDAO.deleteBook(bookId);
    }

    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookDAO.searchBooks(keyword.trim());
    }

    private void validateBook(Book book) throws LibraryException {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new LibraryException("Book title cannot be empty.");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new LibraryException("Book author cannot be empty.");
        }
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new LibraryException("Book ISBN cannot be empty.");
        }
        if (book.getTotalCopies() < 0) {
            throw new LibraryException("Total copies cannot be negative.");
        }
        if (book.getAvailableCopies() < 0) {
            throw new LibraryException("Available copies cannot be negative.");
        }
        if (book.getAvailableCopies() > book.getTotalCopies()) {
            throw new LibraryException("Available copies cannot be greater than total copies.");
        }
    }
}
