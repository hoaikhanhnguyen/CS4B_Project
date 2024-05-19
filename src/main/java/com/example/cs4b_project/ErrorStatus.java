package com.example.cs4b_project;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ErrorStatus {
    @FXML
    Button errorExit;
    public void initialize(){
        // Listeners
        errorExit.setOnAction((ActionEvent again) -> {
            Platform.exit();
            System.exit(0);
        });
    }
}
