package com.example.cs4b_project;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {

    final private ServerSocket serverSocket;

    public GameServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                System.out.println("Waiting for players to connect...");
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected.");

                // separate handler method to a class that implements runnable interface.
                // runnable interface runs each instance of a class on a separate thread
                ClientHandler clientHandler = new ClientHandler(socket, "server");

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }

    // separate error handler class to avoid nested try catch blocks
    public void closeServerSocket() {
        try {
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException e){
            throw new RuntimeException(e); // instead of e.printStackTrace()
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket (1234);
        com.example.cs4b_project.GameServer server = new com.example.cs4b_project.GameServer(serverSocket);
        server.startServer();
        System.out.println("Server started on port 1234.");
    }
}
