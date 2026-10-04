package com.lms;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Main entry point for the Library Management System.
 * This class launches the JavaFX application.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Simple welcome screen to verify JavaFX is working
        Label titleLabel = new Label("Library Management System");
        titleLabel.setFont(new Font("Arial", 28));

        Label subtitleLabel = new Label("Welcome! The application is running.");
        subtitleLabel.setFont(new Font("Arial", 16));

        VBox root = new VBox(20, titleLabel, subtitleLabel);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #2b2d42; -fx-padding: 40;");
        titleLabel.setStyle("-fx-text-fill: #edf2f4;");
        subtitleLabel.setStyle("-fx-text-fill: #8d99ae;");

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Library Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
