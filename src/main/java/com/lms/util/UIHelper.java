package com.lms.util;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class UIHelper {
    public static <T> void addSNoColumn(TableView<T> table) {
        TableColumn<T, Void> indexCol = new TableColumn<>("S.No");
        indexCol.setPrefWidth(45);
        indexCol.setMaxWidth(55);
        indexCol.setMinWidth(40);
        indexCol.setStyle("-fx-alignment: CENTER;");
        
        indexCol.setCellFactory(col -> new TableCell<T, Void>() {
            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });
        
        table.getColumns().add(0, indexCol);
    }
}
