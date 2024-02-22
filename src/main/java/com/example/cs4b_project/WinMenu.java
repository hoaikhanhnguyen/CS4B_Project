package com.example.cs4b_project;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WinMenu {
    @FXML
    public Label winnerLabel;
    private static final String WINNER_FORMAT = "Player %d is the Winner!";
    public void setWinner(int player) {
        if (player != 3)
            winnerLabel.setText(String.format(WINNER_FORMAT, player));
        else
            winnerLabel.setText("The game is a Tie!");
    }
}
