package com.example.cs4b_project;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
public class Gameboard {

    @FXML
    public Button button1, button2, button3, button4, button5, button6, button7, button8, button9;
    @FXML
    public Button[] boardArray;

    public Game game;
    public int player = 1;

    public void initialize() {
        boardArray =
            new Button[]{button1, button2, button3,
                         button4, button5, button6,
                         button7, button8, button9};
        game = new Game();
        for (int i = 0 ; i < 9 ; i++) {
            int buttonPos = i;
            boardArray[i].setOnAction((ActionEvent a) -> {
                boardArray[buttonPos].setText(player == 1 ? "X" : "O" );
                boardArray[buttonPos].setDisable(true);

                // Set Internal Board
                game.setPos(buttonPos, player);

                // Update Player
                player = 1+(player%2);

                game.dumpBoard();
                a.consume();
            });
        }
    }

}
