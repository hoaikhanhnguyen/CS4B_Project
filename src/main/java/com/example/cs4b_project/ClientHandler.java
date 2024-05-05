package com.example.cs4b_project;

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

            try {
                bufferedWriter.write("SERVERNAME:" + usr);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                System.out.println("ERROR: Sending ClientHandler username back to ChatClient fails.");
                e.printStackTrace();
            }

            //broadcastMessage(" (SERVER) " + clientUserName + " has entered the chat.");
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
                if (messageFromClient != null) {
                    //broadcastMessage(messageFromClient);
                }
            } catch (IOException e) {
                closeEverything();
                break;
            }
        }
    }

//    public void broadcastMessage(String messageToSend) {
//        // loop through array list and send msg to all clients
//        for (ClientHandler clientHandler : clientHandlers) {
//            try {
//                clientHandler.bufferedWriter.write(clientUserName + ": " +messageToSend);
//                //explicitly send newline
//                clientHandler.bufferedWriter.newLine();
//                clientHandler.bufferedWriter.flush();
//            } catch(IOException e) {
//                closeEverything();
//            }
//        }
//    }

    public static void removeClientHandler(String internalUserName) { // assuming all clientHandlers have distinct usernames.
        for (int i = 0 ; i < clientHandlers.size() ; i++) {
            if (clientHandlers.get(i).clientUserName.equals(internalUserName)) {
                //clientHandlers.get(i).broadcastMessage(internalUserName + " has left the chat.");
                clientHandlers.remove(i);

                System.out.println("Successfully removed " + internalUserName);
                break;
            }
        }
    }

    // when client leaves chat
    public void removeClientHandler() {
        clientHandlers.remove(this);
        //broadcastMessage(clientUserName + " has left the chat.");
    }

    public synchronized void closeEverything() {
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