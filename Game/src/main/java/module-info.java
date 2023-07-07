module Game {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;

    opens View to javafx.fxml;
    exports View;

    opens Model to com.google.gson;
    exports Model;

}