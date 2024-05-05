package com.example.cs4b_project;

import java.net.*;
/*import java.net.*;
import java.io.*;
class Server{
    public static void main(String args[])throws Exception{

        ServerSocket ss = new ServerSocket(3333);
        Socket s = ss.accept();
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String str="",str2="";

        while(!str.equals("stop")){
            str = din.readUTF();
            System.out.println("client says: " + str);
            str2 = br.readLine();
            dout.writeUTF(str2);
            dout.flush();
        }

        din.close();
        s.close();
        ss.close();
    }
}
}
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 1234;
    private ServerSocket serverSocket;
    private Socket client1;
    private Socket client2;
    private ObjectOutputStream client1Out;
    private ObjectOutputStream client2Out;

    public Server() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Game server is running on port " + PORT);
    }

    public void acceptConnections() {
        try {
            System.out.println("Waiting for players to join...");

            client1 = serverSocket.accept();
            System.out.println("Player 1 connected.");
            client1Out = new ObjectOutputStream(client1.getOutputStream());
            client1Out.writeObject("You are connected as Player 1. Waiting for Player 2...");

            client2 = serverSocket.accept();
            System.out.println("Player 2 connected.");
            client2Out = new ObjectOutputStream(client2.getOutputStream());
            client2Out.writeObject("You are connected as Player 2.");

            client1Out.writeObject("Both players are connected. \nLet's play Tic Tac Toe!");
            client2Out.writeObject("Both players are connected. \nLet's play Tic Tac Toe!");

            keepConnection(client1, client1Out);
            keepConnection(client2, client2Out);

        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void keepConnection(Socket client, ObjectOutputStream out) {
        new Thread(() -> {
            try {
                while (true) {}
            } catch (Exception e) {
                System.out.println("Error with client connection: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.acceptConnections();
    }
}
