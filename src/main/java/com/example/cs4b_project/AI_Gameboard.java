package com.example.cs4b_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.Random;

import java.io.IOException;

public class AI_Gameboard {

    @FXML
    public Button button1, button2, button3, button4, button5, button6, button7, button8, button9, buttonNewGame;
    @FXML
    public Label turnCount, playerOneLabel, playerTwoLabel;
    @FXML
    public Label playerOneWinCount, playerTwoWinCount;
    @FXML
    public Button[] boardArray;

    public Game game;
    public int playerOneWins = 0;
    public int playerTwoWins = 0;
    public int player = 1;

    public void initialize() {
        boardArray =
            new Button[]{button1, button2, button3,
                         button4, button5, button6,
                         button7, button8, button9};
        game = new Game();

        // Set Initial Player Turn Indicator
        updatePlayerTurnInd(player);

        for (int i = 0 ; i < 9 ; i++) {
            int buttonPos = i;
            boardArray[i].setOnAction((ActionEvent a) -> {
                player = 1;
                boardArray[buttonPos].setText("X");
                boardArray[buttonPos].setDisable(true);

                // Update turn count
                updateTurnCount();

                // Set Internal Board
                game.setPos(buttonPos, 1);

                // Check for win:
                if (game.isComplete()) {
                    gameWon(1);
                } else {
                    // Make AI move
                    ai_move(boardArray, player);

                    // Update turn count
                    updateTurnCount();
                    // Update Player Turn Indicator

                    if (game.isComplete()) {
                        gameWon(2);
                    }
                }

                game.dumpBoard();
                a.consume();
            });

        }
        // resets board when "New Game" is pressed
        buttonNewGame.setOnAction((ActionEvent newGame) -> {
            restartGame();
            newGame.consume();
        });
    }

    public void gameWon(int player) {

        if (player != 0) {
            if (player == 1) {
                playerOneWins++;
                playerOneWinCount.setText(Integer.toString(playerOneWins));
            }
            if (player == 2) {
                playerTwoWins++;
                playerTwoWinCount.setText(Integer.toString(playerTwoWins));
            }
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("win-menu.fxml"));
            // Load stage onto scene
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);

            // Set up WinMenu controller with proper winner
            WinMenu winMenuController = fxmlLoader.getController();
            winMenuController.setWinner(player);

            // Set modality to WINDOW_MODAL so that user cannot interact with board
            stage.initModality(Modality.APPLICATION_MODAL);

            // Show the stage
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println("There was an error opening the win screen.");
        }
        restartGame();
    }

    public void restartGame() {
        for (int j = 0; j < 9; j++) {
            boardArray[j].setText("");
            boardArray[j].setDisable(false);
        }
        player = 1;
        updatePlayerTurnInd(player);
        game.reset();
        turnCount.setText("0");
        game.dumpBoard();
    }

    public void updateTurnCount() {
        int turns = Integer.parseInt(turnCount.getText());
        turns++;
        turnCount.setText(Integer.toString(turns));
    }

    public void updatePlayerTurnInd(int player) {
        if (player == 1) {
            playerOneLabel.setText("Player 1's Turn!");
            playerOneLabel.setUnderline(true);
            playerTwoLabel.setText("Player 2");
            playerTwoLabel.setUnderline(false);
        } else {
            playerOneLabel.setText("Player 1");
            playerOneLabel.setUnderline(false);
            playerTwoLabel.setText("Player 2's Turn!");
            playerTwoLabel.setUnderline(true);

        }
    }

    public void ai_move(Button[] boardArray, int player) {
        Random random = new Random();
        int ai_pos = random.nextInt(9);

        boolean availableCell = false;
        for (int i = 0; i < 9; i++) {
            if (!boardArray[ai_pos].getText().isEmpty()) {
                availableCell = true;
            }
        }

        if (availableCell) {
            do {
                ai_pos = random.nextInt(9);
            } while (!boardArray[ai_pos].getText().isEmpty());
        }

        boardArray[ai_pos].setText("O");
        boardArray[ai_pos].setDisable(true);
        System.out.println("Player " + 2);
        game.setPos(ai_pos, 2);
    }

}
