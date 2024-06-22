module com.footballmanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.footballmanager to javafx.fxml;
    exports com.footballmanager;
}