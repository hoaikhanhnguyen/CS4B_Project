package com.example.cs4b_project;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WinMenu {
    @FXML
    public Label winnerLabel;
    private static final String WINNER_FORMAT = "Player %d is the Winner!";
    public void setWinner(int player) {
        winnerLabel.setText(String.format(WINNER_FORMAT, player));
    }
}
