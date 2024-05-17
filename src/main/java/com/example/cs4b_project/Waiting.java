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

    }



    public void openBoard() throws IOException {
        Stage stage = (Stage) hostB.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("online_game.fxml"));
        fxmlLoader.setController(new Online_game());
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
