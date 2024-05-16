package com.example.cs4b_project;

import com.example.cs4b_project.Messages.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {

    //singleton to store a single game session
    private static class Session {
        private Socket client1;
        private Socket client2;
        private GameInstance gameSession;

    }

    private static final int PORT = 1234;
    private static ServerSocket serverSocket;
    //private Socket client1;--
    //private Socket client2;--
    private static ObjectOutputStream client1Out;
    private static ObjectOutputStream client2Out;
    private static ObjectInputStream client1In;
    private static ObjectInputStream client2In;

    private char clientId;

    private static ArrayList<ObjectOutputStream> clientOutputStreams = new ArrayList<ObjectOutputStream>();

    public static ArrayList<Session> sessions;

    // Debug/Temporary variable to store the single game in the server
    private static Game game;
    private static Session session;
    private static int sessionCount = 0;

    public Server() throws IOException {
        serverSocket = new ServerSocket(PORT);
        sessions = new ArrayList<>();
        sessions.add(new Session());
        session = sessions.get(sessionCount);
        session.gameSession = new GameInstance();
        game = session.gameSession.getGame();
        sessionCount++;

        System.out.println("Game server is running on port " + PORT);
    }

    public static void newSession(){
        sessions.add(new Session());
        session = sessions.get(sessionCount);
        session.gameSession = new GameInstance();
        game = session.gameSession.getGame();
        sessionCount++;
        clientOutputStreams = new ArrayList<ObjectOutputStream>();
        acceptConnections();
    }

    public static void acceptConnections() {
        new Thread(()-> {
            try {
                System.out.println("Waiting for players to join...");

                session.client1 = serverSocket.accept();
                System.out.println("Player 1 connected.");
                client1Out = new ObjectOutputStream(session.client1.getOutputStream());
                client1Out.writeObject(new WhichPlayerMessage(WhichPlayerMessage.PLAYER_1));
                clientOutputStreams.add(client1Out);
                if (clientOutputStreams.size() == 1) {
                    client1Out.flush();
                    client1Out.writeObject(new TextMessage("Waiting for Player 2..."));
                }

                session.client2 = serverSocket.accept();
                System.out.println("Player 2 connected.");
                client2Out = new ObjectOutputStream(session.client2.getOutputStream());
                client2Out.writeObject(new WhichPlayerMessage(WhichPlayerMessage.PLAYER_2));
                clientOutputStreams.add(client2Out);


                if (clientOutputStreams.size() == 2) {
                    client1Out.flush();
                    client2Out.flush();
                    client1Out.writeObject(new TextMessage("Both players are connected. Let's play!"));
                    client2Out.writeObject(new TextMessage("Both players are connected. Let's play!"));
                    client1Out.writeObject(new StatusMessage(StatusMessage.START_GAME, game.getBoard()));
                    client1Out.writeObject(new StatusMessage(StatusMessage.START_GAME, game.getBoard()));
                }

                // Game loop
                client1Out.writeObject(new StatusMessage(StatusMessage.MAKE_MOVE, game.getBoard()));
                client2Out.writeObject(new StatusMessage(StatusMessage.WAITING, game.getBoard()));

                keepConnection(session.client1, client1Out, session);
                keepConnection(session.client2, client2Out, session);

                client1In = new ObjectInputStream(session.client1.getInputStream());
                client2In = new ObjectInputStream(session.client2.getInputStream());


                keepConnection(clientOutputStreams, session.client1, session, client1In);
                keepConnection(clientOutputStreams, session.client2, session, client2In);

                System.out.println("Session is full. Creating new session.");
                newSession();
            }catch (Exception e){
                System.out.println("ERROR!!!");
            }
        }).start();


    }

    private static void keepConnection(Socket client, ObjectOutputStream out, Session session) {
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

    private static void keepConnection(ArrayList<ObjectOutputStream> streams, Socket client, Session currentSession, ObjectInputStream in) {
        try {
            new Thread(() -> {
                try {
                    while (true) {
                        Object move = in.readObject();
                        System.out.println("OBJECT READ");
                        System.out.println("From Client: " + client);
                        System.out.println("Printing input: " + move);

                        if(client == currentSession.client1) {
                            streams.get(1).writeObject(new StatusMessage(StatusMessage.MAKE_MOVE, currentSession.gameSession.getGame().getBoard()));
                            streams.get(0).writeObject(new StatusMessage(StatusMessage.WAITING, currentSession.gameSession.getGame().getBoard()));

                            streams.get(1).writeObject(move);
                        }else if (client == currentSession.client2 ) {
                            streams.get(0).writeObject(new StatusMessage(StatusMessage.MAKE_MOVE, currentSession.gameSession.getGame().getBoard()));
                            streams.get(1).writeObject(new StatusMessage(StatusMessage.WAITING, currentSession.gameSession.getGame().getBoard()));

                            streams.get(0).writeObject(move);
                        }
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
