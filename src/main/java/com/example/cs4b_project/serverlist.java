package com.example.cs4b_project;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class serverlist {

    public void closeWindow(ActionEvent actionEvent) {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();        //closes the ui window silently
    }
}
