package com.example.cs4b_project;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
public class Gameboard {

    @FXML
    public Button button1, button2, button3, button4, button5, button6, button7, button8, button9;
    @FXML
    public Button[] boardArray;

    public void initialize() {
        boardArray =
            new Button[]{button1, button2, button3,
                         button4, button5, button6,
                         button7, button8, button9};
        for (int i = 0 ; i < 9 ; i++) {
            int buttonPos = i;
            boardArray[i].setOnAction((ActionEvent a) -> {
                // TODO - Implement proper interaction with a back-end tic-tac-toe board
                boardArray[buttonPos].setText("X");
                boardArray[buttonPos].setDisable(true);
                a.consume();
            });
        }
    }
}
