package com.example.cs4b_project;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
        stage.setOnCloseRequest(windowEvent -> {
            System.out.println("closed the program from board");
            try{
                Platform.exit();
                System.exit(0);
            }catch (Exception e1){
                e1.printStackTrace();
            }
        });
        stage.setScene(scene);
    }
    public void open1Player() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gameboard.fxml"));
        fxmlLoader.setController(new AI_Gameboard());
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void openGameList() throws IOException {
        Stage stage = (Stage) joinB.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gamelist.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
}
