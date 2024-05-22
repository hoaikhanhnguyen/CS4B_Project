package com.example.cs4b_project;

import com.example.cs4b_project.ChatRoom.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class Chatroom {

    @FXML
    public TextArea textInput;

    @FXML
    public TextField textIp, textPort;

    public Button exitB;

    @FXML
     public VBox containerV;

    String message ="";
    int user;
    String userName = null;

    Socket socket;
    ChatClient client;

    public void initialize() throws IOException {
        Main.clients++;                                 //keeps track of # of clients
        user = Main.clients;                            //for username

        if(userName == null)
            userName = "user#" + user + ": ";

        //socket stuff
        //mainly for debugging
        socket = new Socket("localhost", 1234);
        client = new ChatClient(socket, userName);
        client.listenForMessage(socket, containerV);
    }

    //remove the socket stuff and uncomment this for the ip field/port field/and connect button to work
    public void run() throws IOException {
//        String ip = textIp.getText();
//        String port = textPort.getText();
//        socket = new Socket(ip, Integer.parseInt(port));
//        client = new ChatClient(socket, userName);
//        client.listenForMessage(socket, containerV);
    }


    public void sendButton(ActionEvent actionEvent) {
        message = textInput.getText();                          //stores text in textarea as message [usr input]
        textInput.setText("");
//        if(!message.isEmpty()) {
//            makeLabel(message);
//        }
        client.sendMessage(message);
    }

    public void makeLabel(String message){
        Label newLabel = new Label(userName + message);
        newLabel.setWrapText(true);
        containerV.getChildren().add(newLabel);
    }
    public void closeWindow(ActionEvent actionEvent) {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();        //closes the ui window silently

        //------------------------------------------------------
        this.client.closeEverything();  //HAS THREADING ISSUES
        //------------------------------------------------------
    }
}
