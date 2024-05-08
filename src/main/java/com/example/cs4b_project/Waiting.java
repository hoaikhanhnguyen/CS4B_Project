package com.example.cs4b_project;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.*;
import java.net.Socket;

public class Waiting {
    public Button joinB;
    public Button hostB;

    private ObjectOutputStream toServer = null;
    private ObjectInputStream fromServer = null;


    int user;
    String userName = null;

    Socket socket;

    public void initialize() throws IOException {
        Main.clients++;                                 //keeps track of # of clients
        user = Main.clients;                            //for username

        if(userName == null)
            userName = "user#" + user + ": ";

        socket = new Socket("localhost", 1234);
        System.out.println("connected as: " + userName);
        toServer = new ObjectOutputStream(socket.getOutputStream());
        fromServer = new ObjectInputStream(socket.getInputStream());
        //System.out.println("client has connected!");
        //System.out.println("client has connected!");
        try {
            Object message;
            while ((message = fromServer.readObject()) != null) {
                String text = (String) message;
                System.out.println(text);
            }
        }catch (Exception e){

            }
    }


    private void handleServerMessages() {
        try {
            Object message;
            while ((message = fromServer.readObject()) != null)
            {
//                String text = (String) message;
//                javafx.application.Platform.runLater(() -> {
//                    textArea.appendText("Server: " + text + "\n");
//                    if (text.startsWith("MOVE")) {
//                        int index = Integer.parseInt(text.split(" ")[1]);
//                        buttons[index].setText("X");
//                    }
//                });
            }
        } catch (Exception e) {
//            javafx.application.Platform.runLater(() ->
//                    textArea.appendText("Connection error: " + e.getMessage() + "\n")
            //);
        } finally {
           // closeResources();
        }
    }





    public void openBoard() throws IOException {
        Stage stage = (Stage) joinB.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gameboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    public void openGameList() throws IOException {
        Stage stage = (Stage) joinB.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gamelist.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
}
