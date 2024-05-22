package com.example.cs4b_project;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    @FXML
    private Button playAgainButton;
    @FXML
    private Button startOnlineGame;

    @FXML
    protected void open1Player() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gameboard.fxml"));
        fxmlLoader.setController(new AI_Gameboard());
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void open2Player() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gameboard.fxml"));
        fxmlLoader.setController(new Gameboard());
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void openOnlinePlayer_ver2() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("waiting.fxml"));
        //fxmlLoader.setController(new Online_game());
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        // Closes the win scene and opens the previous (gameboard)
        Stage currentScene = (Stage) startOnlineGame.getScene().getWindow();
        currentScene.close();
    }

//    @FXML
//    protected void openOnlinePlayer() throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("waiting.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        Stage stage = new Stage();
//        stage.setScene(scene);
//        stage.show();
//    }

    @FXML
    public void closeWindow() {
        Platform.exit();
        System.exit(0);
    }
}