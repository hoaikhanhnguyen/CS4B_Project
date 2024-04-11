package com.example.cs4b_project.ChatRoom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    // list of clients
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public String clientUserName;

    public ClientHandler(Socket socket, String usr) {
        try {
            this.socket = socket;

            // Each socket has an input and output stream
            // In Java there are byte streams and char streams. Char streams end with 'Writer'
            // Wrap the byte stream in a char stream to output chars
            // Then wrap char stream with buffered writer
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //this.clientUserName = bufferedReader.readLine(); // get client username when connecting
            this.clientUserName = usr;
            clientHandlers.add(this);
            broadcastMessage( clientUserName + " has entered the chat.");
        } catch(IOException e) {
            closeEverything();
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything();
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        // loop through array list and send msg to all clients
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUserName.equals(clientUserName)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    ChatClient.setSender(clientUserName);                   //sends the client user to chatclient for printing of the username [ username: hi]
                    //explicitly send newline
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch(IOException e) {
                closeEverything();
            }
        }
    }

    // when client leaves chat
    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage(clientUserName + " has left the chat.");
    }

    public synchronized void closeEverything() {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            System.out.println("closed reader");
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            System.out.println("closed writer");
            if (socket != null) {
                socket.close();
            }
            System.out.println("closed socket");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
