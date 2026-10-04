package com.lms.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String PROPERTIES_FILE = "/db.properties";
    private static String url;
    private static String user;
    private static String password;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = DatabaseConnection.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                System.err.println("Sorry, unable to find " + PROPERTIES_FILE + ". Using default connection settings.");
                // Fallback defaults
                url = "jdbc:mysql://localhost:3306/lms_db";
                user = "root";
                password = "";
                return;
            }

            Properties prop = new Properties();
            prop.load(input);

            url = prop.getProperty("db.url");
            user = prop.getProperty("db.user");
            password = prop.getProperty("db.password");

        } catch (IOException ex) {
            System.err.println("Error reading " + PROPERTIES_FILE);
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
