package com.lms.service;

import com.lms.dao.UserDAO;
import com.lms.model.User;

public class AuthService {
    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public User authenticate(String username, String password) throws LibraryException {
        if (username == null || username.trim().isEmpty()) {
            throw new LibraryException("Username cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new LibraryException("Password cannot be empty.");
        }

        User user = userDAO.findByUsername(username);

        if (user == null) {
            throw new LibraryException("Invalid username or password.");
        }

        if (user.getPassword().equals(password)) {
            return user;
        } else {
            throw new LibraryException("Invalid username or password.");
        }
    }
}
