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

    public void updateCredentials(String currentUsername, String currentPassword, String newUsername, String newPassword) throws LibraryException {
        // First verify current credentials
        User user = authenticate(currentUsername, currentPassword);
        
        if (newUsername == null || newUsername.trim().isEmpty()) {
            throw new LibraryException("New username cannot be empty.");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new LibraryException("New password cannot be empty.");
        }
        
        // Check if new username is already taken by someone else
        User existingUser = userDAO.findByUsername(newUsername);
        if (existingUser != null && existingUser.getUserId() != user.getUserId()) {
            throw new LibraryException("Username '" + newUsername + "' is already taken.");
        }
        
        user.setUsername(newUsername);
        user.setPassword(newPassword);
        
        boolean updated = userDAO.updateUser(user);
        if (!updated) {
            throw new LibraryException("Failed to update credentials in the database.");
        }
    }
}
