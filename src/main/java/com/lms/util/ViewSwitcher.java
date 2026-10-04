package com.lms.util;

import com.lms.controller.MainLayoutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewSwitcher {
    private static MainLayoutController mainController;

    public static void setMainController(MainLayoutController controller) {
        mainController = controller;
    }

    public static void loadView(String fxmlPath) {
        if (mainController != null) {
            mainController.loadCenterView(fxmlPath);
        }
    }

    public static void setStatus(String message) {
        if (mainController != null) {
            mainController.setStatus(message);
        }
    }

    /**
     * Opens a dialog window, allows controller initialization, and then blocks until closed.
     */
    public static <T> void openDialog(String fxmlPath, String title, java.util.function.Consumer<T> controllerInitializer) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewSwitcher.class.getResource(fxmlPath));
            Parent root = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(ViewSwitcher.class.getResource("/css/style.css").toExternalForm());
            dialogStage.setScene(scene);
            
            T controller = loader.getController();
            if (controllerInitializer != null) {
                controllerInitializer.accept(controller);
            }
            
            dialogStage.showAndWait(); // Block until closed
            
        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showError("Error", "Could not load dialog: " + fxmlPath);
        }
    }
    
    public static void openDialog(String fxmlPath, String title) {
        openDialog(fxmlPath, title, null);
    }
}
