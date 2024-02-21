module com.example.gamewindow {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.gamewindow to javafx.fxml;
    exports com.example.gamewindow;
}