package com.example.cs4b_project;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class WinMenu extends Gameboard{
    @FXML
    public Label winnerLabel;
    @FXML
    public Button playAgain, exitB;

    private Scene prvScene;

    private static final String WINNER_FORMAT = "Player %d is the Winner!";

    public void initialize(){
        // Listeners
        playAgain.setOnAction((ActionEvent again) -> {
            playAgain();
            again.consume();
        });
        exitB.setOnAction((ActionEvent again) -> {
            closeWindow();
        });
    }

    public void setWinner(int player) {
        if (player != 3)
            winnerLabel.setText(String.format(WINNER_FORMAT, player));
        else
            winnerLabel.setText("The game is a Tie!");
    }

    // Opens the gameboard screen again
    public void playAgain() {
        // Object for previous scene (gameboard)
        Stage stage = new Stage();
        stage.setScene(prvScene);

        // Closes the win scene and opens the previous (gameboard)
        Stage curScene = (Stage) playAgain.getScene().getWindow();
        curScene.close();
        stage.show();
    }

    public void closeWindow() {
        Platform.exit();
        System.exit(0);
    }

    // Setter for the previous screen
    public void setPrvScene(Scene cur){
        this.prvScene = cur;
    }
}
