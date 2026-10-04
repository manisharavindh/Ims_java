module com.lms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.lms to javafx.fxml;

    exports com.lms;
}
