package com.example.cs4b_project;

import com.example.cs4b_project.Messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
    private static final int PORT = 1234;
    private ServerSocket serverSocket;
    private Socket client1;
    private Socket client2;
    private ObjectOutputStream client1Out;
    private ObjectOutputStream client2Out;
    private char clientId;

    private static ArrayList<ObjectOutputStream> clientOutputStreams = new ArrayList<ObjectOutputStream>();
    private ArrayList<Socket> connections;

    private ArrayList<GameInstance> games;

    public Server() throws IOException {
        serverSocket = new ServerSocket(PORT);
        connections = new ArrayList<>();
        System.out.println("Game server is running on port " + PORT);
    }

    public void acceptConnections() {
        try {
            System.out.println("Waiting for players to join...");

            client1 = serverSocket.accept();
            System.out.println("Player 1 connected.");
            client1Out = new ObjectOutputStream(client1.getOutputStream());
            client1Out.writeObject(new TextMessage("You are connected as Player 1."));
            clientOutputStreams.add(client1Out);
            if (clientOutputStreams.size() == 1) {
                client1Out.flush();
                client1Out.writeObject(new TextMessage("Waiting for Player 2..."));
            }

            client2 = serverSocket.accept();
            System.out.println("Player 2 connected.");
            client2Out = new ObjectOutputStream(client2.getOutputStream());
            client2Out.writeObject(new TextMessage("You are connected as Player 2."));
            clientOutputStreams.add(client2Out);


            if (clientOutputStreams.size() == 2) {
                client1Out.flush();
                client2Out.flush();
                client1Out.writeObject(new TextMessage("Both players are connected. Let's play!"));
                client2Out.writeObject(new TextMessage("Both players are connected. Let's play!"));
            }

            keepConnection(client1, client1Out);
            keepConnection(client2, client2Out);


        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void keepConnection(Socket client, ObjectOutputStream out) {
        try {
            new Thread(() -> {
                try {
                    while (true) {
                        // wait for messages
                    }
                } catch (Exception e) {
                    System.out.println("Error with client connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.acceptConnections();
    }
}
