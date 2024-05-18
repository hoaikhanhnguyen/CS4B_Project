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
import java.util.ArrayList;
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

    private ArrayList<Integer> disabledButtons;

    public Game game;
    public int playerOneWins = 0;
    public int playerTwoWins = 0;
    public int player;

    public void gameWon(int player) {

        if (player != 0) {
            if (player == 1) {
                playerOneWins++;
                playerOneWinCount.setText(Integer.toString(playerOneWins));
                sendMove(10);
                sendMove(80);
            }
            if (player == 2) {
                playerTwoWins++;
                playerTwoWinCount.setText(Integer.toString(playerTwoWins));
                sendMove(20);
                sendMove(90);
            }
        }

        displayWinScreen(player);
        restartGame();
        sendMove(40);
    }


    public void restartGame() {
        for (int j = 0; j < 9; j++) {
            boardArray[j].setText("");              // this allows for an isEmpty() check
            boardArray[j].setDisable(false);
        }
        disabledButtons.clear();
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

    public void updateWinsP1(){
        playerOneWins++;
        playerOneWinCount.setText(Integer.toString(playerOneWins));
    }
    public void updateWinsP2(){
        playerTwoWins++;
        playerTwoWinCount.setText(Integer.toString(playerTwoWins));
    }

    public void indicatorStateP1(){
        playerOneLabel.setText("Player 1's Turn!");
        playerOneLabel.setUnderline(true);
        playerOneLabel.setTextFill(Color.web("#00ff15"));
        playerTwoLabel.setText("Player 2");
        playerTwoLabel.setTextFill(Color.web("#000000"));
        playerTwoLabel.setUnderline(false);
    }

    public void indicatorStateP2(){
        playerOneLabel.setText("Player 1");
        playerOneLabel.setUnderline(false);
        playerOneLabel.setTextFill(Color.web("#000000"));
        playerTwoLabel.setText("Player 2's Turn!");
        playerTwoLabel.setTextFill(Color.web("#00ff15"));
        playerTwoLabel.setUnderline(true);
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

        disabledButtons = new ArrayList<Integer>();
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
                disableAllButtons();
                sendMove(buttonPos);

                // Set Internal Board
                game.setPos(buttonPos, player);

                // Update turn count
                updateTurnCount();

                if (game.isComplete() == 1) {
                    gameWon(1);
                    //sendMove(10);
                } else if (game.isComplete() == 2) {
                    gameWon(2);
                    //sendMove(20);
                } else if (game.isComplete() == 3) {
                    gameWon(3);
                    //sendMove(30);
                }

                game.dumpBoard();
                a.consume();
            });

        }

        // resets board when "New Game" is pressed
        buttonNewGame.setOnAction((ActionEvent newGame) -> {
            restartGame();
            sendMove(40);
            newGame.consume();
        });
    }

    private synchronized void handleServerMessages() {
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

                            javafx.application.Platform.runLater(() ->
                                    displayWinScreen(1)
                            );
                            disableAllButtons();
                        }
                        case StatusMessage.WINNER_2 -> {
                            System.out.println("Winner is Player 2");

                            javafx.application.Platform.runLater(() ->
                                    displayWinScreen(2)
                            );
                            disableAllButtons();
                        }
                        case StatusMessage.RESTART -> {
                            System.out.println("Restarting the game.");

                            javafx.application.Platform.runLater(this::restartGame);

                        }
                        case StatusMessage.UPDATE_COUNTER -> {
                            System.out.println("Updating the counter.");
                            javafx.application.Platform.runLater(this::updateTurnCount);

                        }
                        case StatusMessage.CHANGE_TURN_1 -> {
                            System.out.println("Updating the turn.");
                            javafx.application.Platform.runLater(this::indicatorStateP1);
                        }
                        case StatusMessage.CHANGE_TURN_2 -> {
                            System.out.println("Updating the turn.");
                            javafx.application.Platform.runLater(this::indicatorStateP2);
                        }
                        case StatusMessage.UPDATE_WIN_COUNTER_1 -> {
                            System.out.println("Updating the wins.");
                            javafx.application.Platform.runLater(this::updateWinsP1);
                        }
                        case StatusMessage.UPDATE_WIN_COUNTER_2 -> {
                            System.out.println("Updating the wins.");
                            javafx.application.Platform.runLater(this::updateWinsP2);
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
        Boolean skip = false;
        for (int i = 0 ; i < 9 ; i++) {
            for(int z = 0; z < disabledButtons.size() ; z++) {
                if(disabledButtons.get(z) == i)
                    skip = true;
            }
            if(skip == false) {
                int buttonPos = i;
                boardArray[buttonPos].setDisable(false);
            }
            skip = false;
        }
    }

    private void disableAllButtons() {
        for (int i = 0 ; i < 9 ; i++) {
            int buttonPos = i;
            boardArray[buttonPos].setDisable(true);
        }
    }
    protected void sendMove(int index) {
        try {
            if (index >= 0 && index <= 9) {
                toServer.writeObject(new MoveMessage(index));

                if (player == 1){
                    indicatorStateP2();
                    sendMove(70);
                }else {
                    indicatorStateP1();
                    sendMove(60);
                }

                sendMove(50);

                System.out.println("Sent Move to Server");
                System.out.println("Selected: " + index);
            } else if (index == 10){
                toServer.writeObject(new StatusMessage(StatusMessage.WINNER_1, game.getBoard()));
                System.out.println("Sent Game Over to Server");
                System.out.println("Game is Over!" + index);
            } else if (index == 20) {
                toServer.writeObject(new StatusMessage(StatusMessage.WINNER_2, game.getBoard()));
                System.out.println("Sent Game Over to Server");
                System.out.println("Game is Over!" + index);
            } else if (index == 30) {
                toServer.writeObject(new StatusMessage(StatusMessage.WINNER_3, game.getBoard()));
                System.out.println("Sent Game Over to Server");
                System.out.println("Game is Over!" + index);
            } else if (index == 40){
                toServer.writeObject(new StatusMessage(StatusMessage.RESTART, game.getBoard()));
                System.out.println("Sent Restart to Server");
                System.out.println("Game is Reset!" + index);
            } else if (index == 50){
                toServer.writeObject(new StatusMessage(StatusMessage.UPDATE_COUNTER, game.getBoard()));
                System.out.println("Sent Update Counter to Server");
                System.out.println("Updated Counter!" + index);
            } else if (index == 60){
                toServer.writeObject(new StatusMessage(StatusMessage.CHANGE_TURN_1, game.getBoard()));
                System.out.println("Sent Change Turn to Server");
                System.out.println("Updated Player(playing) Turn!" + index);
            } else if (index == 70){
                toServer.writeObject(new StatusMessage(StatusMessage.CHANGE_TURN_2, game.getBoard()));
                System.out.println("Sent Change Turn to Server");
                System.out.println("Updated Player(playing) Turn!" + index);
            } else if (index == 80){
                toServer.writeObject(new StatusMessage(StatusMessage.UPDATE_WIN_COUNTER_1, game.getBoard()));
                System.out.println("Sent Update Wins to Server");
                System.out.println("Updated opponent's wins!" + index);
            } else if (index == 90){
                toServer.writeObject(new StatusMessage(StatusMessage.UPDATE_WIN_COUNTER_2, game.getBoard()));
                System.out.println("Sent Update Wins to Server");
                System.out.println("Updated opponent's wins!" + index);
            }
        } catch (Exception ex) {
            javafx.application.Platform.runLater(() ->
                    systemMsg2.setText("Failed to send move to server: " + ex.getMessage() + "\n")
            );
        }
    }

    private void disableButtons() {
        System.out.println("Disabling Buttons");
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
        disabledButtons.add(pos);
        game.setPos(pos, player == 2 ? 1 : 2);
        System.out.println("Set position: " + pos);
        System.out.println("For Player: " + player);
    }

    private void displayWinScreen(int player) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("win-menu.fxml"));
            fxmlLoader.setController(new Online_WinMenu());
            // Load stage onto scene
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);

            // Set up WinMenu controller with proper winner
            Online_WinMenu winMenuController = fxmlLoader.getController();
            winMenuController.setWinner(player);

            // Set modality to WINDOW_MODAL so that user cannot interact with board
            stage.initModality(Modality.APPLICATION_MODAL);

            // Show the stage
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println("There was an error opening the win screen.");
        }
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
