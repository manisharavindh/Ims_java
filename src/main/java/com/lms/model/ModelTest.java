package com.lms.model;

import java.time.LocalDate;

public class ModelTest {
    public static void main(String[] args) {
        System.out.println("Testing Model Classes...");

        // 1. Test Book
        Book book = new Book(1, "The Pragmatic Programmer", "Andrew Hunt", "Software Engineering", "978-0135957059", 5, 5);
        System.out.println(book.toString());
        book.setAvailableCopies(4);
        System.out.println("Updated Available Copies: " + book.getAvailableCopies());

        // 2. Test Member
        Member member = new Member();
        member.setMemberId(1);
        member.setName("Alice");
        member.setEmail("alice@example.com");
        member.setPhone("555-1234");
        member.setDepartment("Mathematics");
        System.out.println(member.toString());

        // 3. Test User
        User user = new User(10, "admin", "secret_password");
        System.out.println(user.toString());
        if (!user.toString().contains("secret_password")) {
            System.out.println("SUCCESS: Password is hidden in toString().");
        } else {
            System.out.println("ERROR: Password exposed in toString()!");
        }

        // 4. Test Transaction
        Transaction transaction = new Transaction(
                100, 
                book.getBookId(), 
                member.getMemberId(), 
                LocalDate.now(), 
                LocalDate.now().plusDays(14), 
                null, 
                "ISSUED"
        );
        System.out.println(transaction.toString());
        
        System.out.println("Model tests completed successfully.");
    }
}
