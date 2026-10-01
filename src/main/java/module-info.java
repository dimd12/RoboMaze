module com.robomaze {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    exports com.robomaze;
    opens com.robomaze.gui to javafx.fxml;
}
