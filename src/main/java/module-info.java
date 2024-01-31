module com.example.cs4b_project {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires kotlin.stdlib;

    opens com.example.cs4b_project to javafx.fxml;
    exports com.example.cs4b_project;
}