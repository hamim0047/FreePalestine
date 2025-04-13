module com.example.palenstine {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires javafx.media;

    opens com.example.palenstine to javafx.fxml;
    exports com.example.palenstine;
}