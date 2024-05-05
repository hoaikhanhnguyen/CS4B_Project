package com.example.cs4b_project;


import java.net.*;
/*import java.net.*;
import java.io.*;
class Client {
    public static void main(String args[])throws Exception{

        Socket s = new Socket("localhost",3333);
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String str = "",str2 = "";

        while(!str.equals("stop")){
            str = br.readLine();
            dout.writeUTF(str);
            dout.flush();
            str2 = din.readUTF();
            System.out.println("Server says: " + str2);
        }

        dout.close();
        s.close();
    }
}
 */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Application {
    private ObjectOutputStream toServer = null;
    private ObjectInputStream fromServer = null;
    private TextArea textArea = new TextArea();
    private Button[] buttons = new Button[9];
    private Socket socket;

    @Override
    public void start(Stage primaryStage) {
        GridPane pane = new GridPane();

        for (int i = 0; i < 9; i++)
        {
            buttons[i] = new Button(" ");
            buttons[i].setPrefSize(100, 100);
            final int finalI = i;
            buttons[i].setOnAction(e -> sendMove(finalI));
            pane.add(buttons[i], i % 3, i / 3);
        }

        Button connectButton = new Button("Connect");
        connectButton.setOnAction(e -> connectToServer());
        pane.add(connectButton, 1, 3);

        textArea.setPrefHeight(100);
        pane.add(textArea, 0, 4, 3, 1);

        Scene scene = new Scene(pane, 300, 400);
        primaryStage.setTitle("TicTacToe Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 3333);
            socket.setKeepAlive(true); // Ensure the connection is kept alive
            toServer = new ObjectOutputStream(socket.getOutputStream());
            fromServer = new ObjectInputStream(socket.getInputStream());

            textArea.appendText("Connected to server.\n");

            new Thread(this::handleServerMessages).start();
        } catch (Exception ex) {
            textArea.appendText("Error: " + ex.getMessage() + "\n");
        }
    }

    private void handleServerMessages() {
        try {
            Object message;
            while ((message = fromServer.readObject()) != null)
            {
                String text = (String) message;
                javafx.application.Platform.runLater(() -> {
                    textArea.appendText("Server: " + text + "\n");
                    if (text.startsWith("MOVE")) {
                        int index = Integer.parseInt(text.split(" ")[1]);
                        buttons[index].setText("X");
                    }
                });
            }
        } catch (Exception e) {
            javafx.application.Platform.runLater(() ->
                    textArea.appendText("Connection error: " + e.getMessage() + "\n")
            );
        } finally {
            closeResources();
        }
    }

    private void sendMove(int index) {
        try {
            toServer.writeObject("MOVE " + index);
            buttons[index].setDisable(true);
        } catch (Exception ex) {
            javafx.application.Platform.runLater(() ->
                    textArea.appendText("Failed to send move to server: " + ex.getMessage() + "\n")
            );
        }
    }

    private void closeResources() {
        try {
            if (fromServer != null) fromServer.close();
            if (toServer != null) toServer.close();
            if (socket != null) socket.close();
            javafx.application.Platform.runLater(() ->
                    textArea.appendText("Disconnected from server.\n")
            );
        } catch (Exception e) {
            javafx.application.Platform.runLater(() ->
                    textArea.appendText("Error while closing resources: " + e.getMessage() + "\n")
            );
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
