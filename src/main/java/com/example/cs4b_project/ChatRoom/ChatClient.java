package com.example.cs4b_project.ChatRoom;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import static com.example.cs4b_project.ChatRoom.ClientHandler.clientHandlers;


public class ChatClient {
    private Socket socket;
    public BufferedReader bufferedReader;
    public BufferedWriter bufferedWriter;
    private String userName;
    private static String trueUser;
    private String internalName;
    private static String senderNum;

    private boolean closing = false;


=======
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;
  
    public ChatClient(Socket socket, String userName) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter((socket.getOutputStream())));
            this.bufferedReader = new BufferedReader((new InputStreamReader(socket.getInputStream())));
            this.userName = userName;
        } catch (IOException e) {
            closeEverything();
        }
    }

    //this takes the username from the client handler in order to close it using the close everything method at the bottom
    public static void setSender(String userT){
        trueUser = userT;
        senderNum = trueUser.substring(trueUser.length() - 1);      //retrieves the client# for printing to UI
        System.out.println(trueUser);
    }

    public void sendMessage(String Message) {
        try {
            //bufferedWriter.write(userName);
            //bufferedWriter.newLine();
            //bufferedWriter.flush();

            //Scanner scanner = new Scanner(System.in);
                String messageToSend = Message;
                bufferedWriter.write(messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
        } catch(IOException e) {
            closeEverything();
        }
    }

    private void setInternalName(String internalName) {
        System.out.println("INTERNAL NAME SET TO: " + internalName);
        this.internalName = internalName;
    }
    public void listenForMessage(Socket socket, VBox box) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage() {
        try {
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(userName + ": "  + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch(IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {
        //create new thread to listen to messages
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
              
                while (socket.isConnected() && !closing) {
                    System.out.println("listening...");
                    try {
                        msgFromGroupChat = bufferedReader.readLine();           //possible threadlock

                        // Synchronize username from ClientHandler
                        // so the ChatClient can close it.
                        if (msgFromGroupChat.indexOf("SERVERNAME:") == 0) {
                            setInternalName(msgFromGroupChat.substring(11));
                            continue;
                        }

                        // Update on-screen chat
                        final String finalMsgFromGroupChat = msgFromGroupChat;
                        Platform.runLater(() -> {                  //runs back on main thread instead of "worker" thread
                            Label newLabel = new Label(finalMsgFromGroupChat);
                            newLabel.setWrapText(true);
                            box.getChildren().add(newLabel);                            //makes object to display in gui
                        });
                    } catch (IOException e) {
                        //e.printStackTrace();
                        closeEverything();
                        break;
                    }
                }

                System.out.println("Listener Thread on " + userName + " closed.");

            }
        }).start();
    }
    //------------------------------------------------------
    //FOR SOME REASON THE PROGRAM CRASHES (PERHAPS DUE TO THREADLOCKING) WHEN CLOSING BUFFERED READER
    //------------------------------------------------------
    public synchronized void closeEverything() {
        if (closing) return;
        System.out.println("START CLOSING ON " + userName);

        closing = true;
        try {
            System.out.println("Attempting to close socket...");
            if (socket != null) {
                socket.close();
            }
            System.out.println("socket closed!");

            System.out.println("Attempting to close bufferedReader...");
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            System.out.println("bufferedReader closed!");

            System.out.println("Attempting to close bufferedWriter...");
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            System.out.println("bufferedWriter closed!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("closing " + internalName);              //closing everything here

        ClientHandler.removeClientHandler(internalName);
        System.out.println("FINISHED CLOSING ON " + userName);
    }



//    public static void main(String[] args) throws IOException {
//
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter your username for the group chat: ");
//        String username = scanner.nextLine();
//        Socket socket = new Socket("localhost", 1234);
//        ChatClient client = new ChatClient(socket, username);
//
//        // Note: these are on separate threads
//        client.listenForMessage();
//        client.sendMessage();
//    }
}
                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for the group chat: ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 1234);
        ChatClient client = new ChatClient(socket, username);

        // Note: these are on separate threads
        client.listenForMessage();
        client.sendMessage();
    }
}
