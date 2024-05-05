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
import java.util.ArrayList;

public class Server {
    private static final int PORT = 1234;
    private ServerSocket serverSocket;
    private Socket client1;
    private Socket client2;
    private ObjectOutputStream client1Out;
    private ObjectOutputStream client2Out;

    private ArrayList<Socket> connections;

    public Server() throws IOException {
        serverSocket = new ServerSocket(PORT);
        connections = new ArrayList<>();
        System.out.println("Game server is running on port " + PORT);
    }

    public void acceptConnections() {
        try {
            System.out.println("Waiting for players to join...");

            while (!serverSocket.isClosed()) {

                Socket client = serverSocket.accept();
                connections.add(client);
                System.out.println("client has connected!");
                keepConnection(client);

            }

        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void keepConnection(Socket client) {
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
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.acceptConnections();
    }
}
