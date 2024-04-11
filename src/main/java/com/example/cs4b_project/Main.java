package com.example.cs4b_project;

import com.example.cs4b_project.ChatRoom.ClientHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.*;

public class Main extends Application {

    static int clients = 0;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Tic Tac Toe!");
        stage.setScene(scene);
        stage.show();
        ServerSocket serverSocket = new ServerSocket (1234);        //port for serversocket & initializes server here
        listenForConnection(serverSocket);
    }

    public void listenForConnection(ServerSocket socket){
        new Thread(new Runnable() {
            @Override
            public void run() {         //initializes the clienthandler here
                try{
                    while(!socket.isClosed()){
                        Socket socket1 = socket.accept();
                         ClientHandler clientHandler = new ClientHandler(socket1, "client" + clients);
                        Thread thread = new Thread(clientHandler);
                        thread.start();
                    }
                }catch (IOException e){
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}