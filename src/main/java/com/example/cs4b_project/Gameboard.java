package com.example.cs4b_project;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
public class Gameboard {

    @FXML
    public Button button1, button2, button3, button4, button5, button6, button7, button8, button9, buttonNewGame;
    public Label turnCount;
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

                // Update turn count
                int turns = Integer.parseInt(turnCount.getText());
                turns++;
                turnCount.setText(Integer.toString(turns));

                game.dumpBoard();
                a.consume();
            });

            // resets board when "New Game" is pressed
            buttonNewGame.setOnAction((ActionEvent newGame) -> {
                for (int j = 0; j < 9; j++) {
                    boardArray[j].setText(" ");
                    boardArray[j].setDisable(false);
                    game.reset();
                    turnCount.setText("0");
                    game.dumpBoard();
                    newGame.consume();
                }
            });
        }
    }

}
