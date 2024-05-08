package com.example.cs4b_project;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class gamelist {

    private int numGames = 0;
    public VBox containerV;

    public void initialize()throws IOException {
        numGames += 1;
        makeLabel("james");
    }

    public void makeLabel(String name){
        Label newLabel = new Label(name + "'s game" + numGames);
        newLabel.setWrapText(true);
        containerV.getChildren().add(newLabel);
    }

    public void closeWindow(ActionEvent actionEvent) {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();        //closes the ui window silently
    }
}
