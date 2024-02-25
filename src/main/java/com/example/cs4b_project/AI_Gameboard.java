package com.example.cs4b_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.Random;

import java.io.IOException;

public class AI_Gameboard extends Gameboard {

    @Override
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
                boardArray[buttonPos].setText("X");
                boardArray[buttonPos].setStyle("-fx-text-fill: #0831e7; -fx-font-size: 28px;");
                boardArray[buttonPos].setDisable(true);

                // Update Player
                player = 1+(player%2);

                // Update turn count
                updateTurnCount();

                // Update Player Turn Indicator
                updatePlayerTurnInd(player);

                // Set Internal Board
                game.setPos(buttonPos, 1);

                // Check for win:
                if (game.isComplete() == 1) {
                    gameWon(1);
                } else {
                    // Make AI move
                    ai_move(boardArray, player);

                    // Update turn count
                    updateTurnCount();

                    if (game.isComplete() == 2) {
                        gameWon(2);
                    }
                    if (game.isComplete() == 3) {
                        gameWon(3);
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

            // Passes the current scene to the win screen logic
            winMenuController.setPrvScene(buttonNewGame.getScene());

            // Set modality to WINDOW_MODAL so that user cannot interact with board
            stage.initModality(Modality.APPLICATION_MODAL);

            // closes the current window before opening the win screen
            Stage curStage = (Stage)buttonNewGame.getScene().getWindow();

            // Show the stage
            stage.showAndWait();
            curStage.close();
        } catch (IOException e) {
            System.out.println("There was an error opening the win screen.");
        }
    }

    public void ai_move(Button[] boardArray, int player) {
        Random random = new Random();
        int ai_pos = random.nextInt(9);

        boolean availableCell = false;
        for (int i = 0; i < 9; i++) {
            if (boardArray[i].getText().isEmpty()) {
                System.out.println("availabe");
                availableCell = true;
            }
        }

        if (availableCell) {
            do {
                ai_pos = random.nextInt(9);
            } while (!boardArray[ai_pos].getText().isEmpty());

            boardArray[ai_pos].setText("O");
            boardArray[ai_pos].setDisable(true);
            boardArray[ai_pos].setStyle("-fx-text-fill: #d40505; -fx-font-size: 28px;");
            System.out.println("Player " + 2);
            game.setPos(ai_pos, 2);
        }
    }

}
