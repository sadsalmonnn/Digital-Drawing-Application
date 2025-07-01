module ca.utoronto.utm.assignment2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.swing;

    opens ca.utoronto.utm.assignment2 to javafx.fxml;
    opens ca.utoronto.utm.assignment2.paint to javafx.fxml;

    exports ca.utoronto.utm.assignment2.scribble;
    exports ca.utoronto.utm.assignment2.paint;

    exports ca.utoronto.utm.assignment2 to javafx.graphics;
}