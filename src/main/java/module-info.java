module com.robomaze {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.robomaze to javafx.fxml;
    exports com.robomaze;
}
