package com.example.cs4b_project;

import com.example.cs4b_project.Messages.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Online_game {
    @FXML
    public Label systemMsg, systemMsg2;
    public BufferedReader bufferedReader;
    public BufferedWriter bufferedWriter;
    private ObjectOutputStream toServer = null;
    private ObjectInputStream fromServer = null;


    int user;
    String userName = null;

    Socket socket;

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
    public int player;

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
        //restartGame();
    }

//    public void restartGame() {
//        for (int j = 0; j < 9; j++) {
//            boardArray[j].setText("");              // this allows for an isEmpty() check
//            boardArray[j].setDisable(false);
//        }
//        //player = 1;
//        updatePlayerTurnInd(player);
//        game.reset();
//        turnCount.setText("0");
//        game.dumpBoard();
//    }

    public void updateTurnCount() {
        int turns = Integer.parseInt(turnCount.getText());
        turns++;
        turnCount.setText(Integer.toString(turns));
    }

    public void updatePlayerTurnInd(int player) {
        if (player == 1) {
            playerOneLabel.setText("Player 1's Turn!");
            playerOneLabel.setUnderline(true);
            playerOneLabel.setTextFill(Color.web("#00ff15"));
            playerTwoLabel.setText("Player 2");
            playerTwoLabel.setTextFill(Color.web("#000000"));
            playerTwoLabel.setUnderline(false);
        } else {
            playerOneLabel.setText("Player 1");
            playerOneLabel.setUnderline(false);
            playerOneLabel.setTextFill(Color.web("#000000"));
            playerTwoLabel.setText("Player 2's Turn!");
            playerTwoLabel.setTextFill(Color.web("#00ff15"));
            playerTwoLabel.setUnderline(true);
        }
    }

    public void initialize() {
        try {
            socket = new Socket("localhost", 1234);
            System.out.println("connected");

            toServer = new ObjectOutputStream(socket.getOutputStream());
            fromServer = new ObjectInputStream(socket.getInputStream());

            new Thread(this::handleServerMessages).start();
        } catch (Exception ex) {
            //System.out.println("Server is not up. Error: " + ex);
            throw new RuntimeException(ex);
        }

        boardArray =
                new Button[]{button1, button2, button3,
                        button4, button5, button6,
                        button7, button8, button9};
        game = new Game();

        // Set Initial Player Turn Indicator
       // updatePlayerTurnInd(player);

        String[] textColors = {"-fx-text-fill: #0831e7;", "-fx-text-fill: #d40505;"};

        for (int i = 0 ; i < 9 ; i++) {
            int buttonPos = i;
            boardArray[i].setOnAction((ActionEvent a) -> {
                String textColor = textColors[player == 1 ? 0 : 1];
                boardArray[buttonPos].setText(player == 1 ? "X" : "O" );
                boardArray[buttonPos].setStyle(textColor + "-fx-font-size: 28px;");
                //boardArray[buttonPos].setDisable(true);
                disableAllButtons();
                sendMove(buttonPos);

                // Set Internal Board
                game.setPos(buttonPos, player);

                // Update turn count
                updateTurnCount();

                // Update Player Turn Indicator
                updatePlayerTurnInd(player);

                if (game.isComplete() == 1) {
                    gameWon(1);
                } else if (game.isComplete() == 2) {
                    gameWon(2);
                } else if (game.isComplete() == 3) {
                    gameWon(3);
                }

                game.dumpBoard();
                a.consume();
            });

        }

        // resets board when "New Game" is pressed
        buttonNewGame.setOnAction((ActionEvent newGame) -> {
            //restartGame();
            newGame.consume();
        });
    }

    private void handleServerMessages() {
        try {
            Object message;
            System.out.println("Msg Received from Server");
            while ((message = fromServer.readObject()) != null)
            {
                Message m = (Message) message;

                if (m.getType().equals("TEXT")) {
                    // Handle TextMessage
                    TextMessage t = (TextMessage) m;
                    javafx.application.Platform.runLater(() ->
                            systemMsg2.setText(t.getMessage())
                    );
                } else if (m.getType().equals("WHICH_PLAYER")) {
                    WhichPlayerMessage w = (WhichPlayerMessage) m;
                    player = w.getPlayer();
                    System.out.println("connected as player " + player);
                    if (player == WhichPlayerMessage.PLAYER_1) {
                        javafx.application.Platform.runLater(() ->
                                systemMsg.setText("You are playing as player 1")
                        );
                    } else if (player == WhichPlayerMessage.PLAYER_2) {
                        javafx.application.Platform.runLater(() ->
                                systemMsg.setText("You are playing as player 2")
                        );
                    }
                }
                else if (m.getType().equals("STATUS")) {
                    StatusMessage s = (StatusMessage) m;
                    switch (s.getStatus()) {
                        case StatusMessage.START_GAME -> {
                            String text = message.toString();
                            startGame(text);
                        }
                        case StatusMessage.WAITING -> {
                            disableAllButtons();
                        }
                        case StatusMessage.MAKE_MOVE -> {
                            enableAllButtons();
                        }
                        case StatusMessage.WINNER_1 -> {
                            System.out.println("Winner is Player 1");
                            disableAllButtons();
                        }
                        case StatusMessage.WINNER_2 -> {
                            System.out.println("Winner is Player 2");
                            disableAllButtons();
                        }
                    }
                }
                else if (m.getType().equals("MOVE")) {
                    MoveMessage move = (MoveMessage) m;
                    System.out.println("UpdatingBoard");
                    updateBoard(move.pos);
                    disableButtons();
                }
                else {
                    // Type of message is unknown - convert to string.
                    String text = message.toString();
                    javafx.application.Platform.runLater(() ->
                            systemMsg2.setText(text)
                    );
                }
            }
        } catch (Exception e) {
            javafx.application.Platform.runLater(() ->
                    systemMsg.setText("Connection error: " + e.getMessage())
            );
            System.out.println(e.getMessage());
        } finally {
            closeResources();
        }
    }
    private void startGame(String text) {
        // make new method and enable after game start
        System.out.println("StartGameFunction");
        javafx.application.Platform.runLater(() ->
                systemMsg2.setText(text)
        );
        if (this.player == WhichPlayerMessage.PLAYER_1) {
            enableAllButtons();
        }
    }

    private void enableAllButtons() {
        for (int i = 0 ; i < 9 ; i++) {
            int buttonPos = i;
            boardArray[buttonPos].setDisable(false);
        }
    }

    private void disableAllButtons() {
        for (int i = 0 ; i < 9 ; i++) {
            int buttonPos = i;
            boardArray[buttonPos].setDisable(true);
        }
    }
    private void sendMove(int index) {
        try {
            toServer.writeObject(new MoveMessage(index));
            System.out.println("Sent Move to Server");
            System.out.println("Selected: " + index);
        } catch (Exception ex) {
            javafx.application.Platform.runLater(() ->
                    systemMsg2.setText("Failed to send move to server: " + ex.getMessage() + "\n")
            );
        }
    }

    private void disableButtons() {
        System.out.println("Enabling Buttons");
        System.out.println(Arrays.toString(game.getBoard()));
        for (int i = 0 ; i < 9 ; i++) {
            if (game.getBoard()[i] != 0) {
                boardArray[i].setDisable(true);
            }
        }
    }

    private void updateBoard(int pos) {
        javafx.application.Platform.runLater(() ->
                boardArray[pos].setText(player == 2 ? "X" : "O" )
        );
        game.setPos(pos, player == 2 ? 1 : 2);
        System.out.println("Set position: " + pos);
        System.out.println("For Player: " + player);
    }

    private void closeResources() {
        try {
            if (fromServer != null) fromServer.close();
            if (toServer != null) toServer.close();
            if (socket != null) socket.close();
            javafx.application.Platform.runLater(() ->
                    systemMsg.setText("Disconnected from server.")
                );
        } catch (Exception e) {
            javafx.application.Platform.runLater(() ->
                    systemMsg.setText("Connection error: " + e.getMessage())
            );
        }
    }

}
