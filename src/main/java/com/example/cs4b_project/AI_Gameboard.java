package com.example.cs4b_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
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
            fxmlLoader.setController(new WinMenu());
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
        restartGame();
    }


    // for optimal AI moves:
    public void ai_move(Button[] board, int player) {
        int highestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        // looping through board
        for (int i = 0; i < 9; i++)
        {
            // if square is empty
            if (board[i].getText().isEmpty())
            {
                // make move for AI temporarily
                board[i].setText("O");

                // calling minMax to scan board
                int score = minMax(board, false, 0);

                // undo move
                board[i].setText("");

                // choosing move with highest score
                if (score > highestScore)
                {
                    highestScore = score;
                    bestMove = i;
                }
            }
        }

        // executing move with highest score (most optimal move)
        if (bestMove != -1)
        {
            board[bestMove].setText("O");
            board[bestMove].setDisable(true);
            board[bestMove].setStyle("-fx-text-fill: #d40505; -fx-font-size: 28px;");
            System.out.println("Player " + 2);
            game.setPos(bestMove, 2);
        }
    }


    /* for random AI moves:
    ai_move(Button[] boardArray, int player) {
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
    } */

    private boolean isFull(Button[] board) {
        for (Button box : board)
        {
            if (box.getText().isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    private int minMax(Button[] board, boolean isMaximal, int depth) {
        // current state
        int score = evaluate(board);

        // base case
        if ((Math.abs(score) == 10) || (isFull(board)))
        {
            return score;
        }

        // if opponent's turn(AI), lets find the move that maximizes score
        if (isMaximal)
        {
            int highestScore = Integer.MIN_VALUE;

            // looping through board trying all available moves
            for (int i = 0; i < 9; i++)
            {
                // if move is possible (cell available)
                if (board[i].getText().isEmpty())
                {
                    board[i].setText("O");

                    // recursive call to find move with highest score (to maximize)
                    highestScore = Math.max(highestScore, minMax(board, false, depth + 1));

                    board[i].setText("");
                }
            }
            return highestScore;
        }

        else  // if human's turn, lets find the move that minimizes the score
        {
            int highestScore = Integer.MAX_VALUE;

            // looping through board trying all available moves
            for (int i = 0; i < 9; i++)
            {
                // if move is possible (cell available)
                if (board[i].getText().isEmpty())
                {
                    board[i].setText("X");

                    // recursive call to find move with lowest score (to minimize), but its absolute value is the highest score
                    // thus we assign it to highestScore
                    highestScore = Math.min(highestScore, minMax(board, true, depth + 1));

                    board[i].setText("");
                }
            }
            return highestScore;
        }
    }


    private int evaluate(Button[] boardArray) {

        // checking all three rows for dub
        for (int row = 0; row < 3; row++)
        {
            if (boardArray[row * 3].getText().equals(boardArray[(row * 3) + 1].getText()) && boardArray[row * 3 + 1].getText().equals(boardArray[(row * 3) + 2].getText()))
            {
                if (boardArray[row * 3].getText().equals("O"))
                {
                    return +10;
                }
                else if (boardArray[row * 3].getText().equals("X"))
                {
                    return -10;
                }
            }
        }

        // checking all three cols for dub
        for (int col = 0; col < 3; col++)
        {
            if (boardArray[col].getText().equals(boardArray[col + 3].getText()) && boardArray[col + 3].getText().equals(boardArray[col + 6].getText()))
            {
                if (boardArray[col].getText().equals("O"))
                {
                    return +10;
                }
                else if (boardArray[col].getText().equals("X"))
                {
                    return -10;
                }
            }
        }

        // checking first diagonal for dub
        if (boardArray[0].getText().equals(boardArray[4].getText()) && boardArray[4].getText().equals(boardArray[8].getText()))
        {
            if (boardArray[0].getText().equals("O"))
            {
                return +10;
            }
            else if (boardArray[0].getText().equals("X"))
            {
                return -10;
            }
        }

        // checking second diagonal for dub
        if (boardArray[2].getText().equals(boardArray[4].getText()) && boardArray[4].getText().equals(boardArray[6].getText()))
        {
            if (boardArray[2].getText().equals("O"))
            {
                return +10;
            }
            else if (boardArray[2].getText().equals("X"))
            {
                return -10;
            }
        }

        // if no winner, then return 0
        return 0;
    }


}




