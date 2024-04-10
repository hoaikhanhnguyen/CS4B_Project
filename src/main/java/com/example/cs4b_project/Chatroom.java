package com.example.cs4b_project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Chatroom {

    @FXML
    public TextArea textInput;

    public VBox container;

    String message ="";
    String user;

    public void initialize(){
        //textInput.setText("Enter a username: tim");
        //user = textInput.getText().substring(textInput.getText().lastIndexOf(':')+1);
        //makeLabel(user);
    }

    public void sendButton(ActionEvent actionEvent) {
        message = textInput.getText();
        textInput.setText("");
        if(!message.isEmpty()) {
            makeLabel(message);
        }
    }

    public void makeLabel(String message){
        Label newLabel = new Label(message);
        newLabel.setWrapText(true);
        container.getChildren().add(newLabel);
    }
    public void closeWindow(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }
}
