package com.example.cs4b_project.ChatRoom;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;

import static com.example.cs4b_project.ChatRoom.ClientHandler.clientHandlers;

public class ChatClient {
    private Socket socket;
    public BufferedReader bufferedReader;
    public BufferedWriter bufferedWriter;
    private String userName;
    private static String trueUser;
    private static String senderNum;

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
    public void listenForMessage(Socket socket, VBox box) {
        //create new thread to listen to messages
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;

                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();           //possible threadlock
                        String finalMsgFromGroupChat = msgFromGroupChat;
                        String finalMsgFromGroupChat1 = msgFromGroupChat;
                        Platform.runLater(()-> {                  //runs back on main thread instead of "worker" thread
                            Label newLabel = new Label("user#" + senderNum + ": "+ finalMsgFromGroupChat1);
                            newLabel.setWrapText(true);
                            box.getChildren().add(newLabel);                            //makes object to display in gui
                        });
                    } catch (IOException e) {
                        closeEverything();
                    }
                }
            }
        }).start();

    }
    //------------------------------------------------------
    //FOR SOME REASON THE PROGRAM CRASHES (PERHAPS DUE TO THREADLOCKING) WHEN CLOSING BUFFERED READER
    //------------------------------------------------------
    public void closeEverything() {
//        try {
//            if (bufferedReader != null) {
//                bufferedReader.close();
//            }
//            if (bufferedWriter != null) {
//                bufferedWriter.close();
//            }
//            if (socket != null) {
//                socket.close();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        System.out.println("closing " + trueUser);              //closing everything here
        for(ClientHandler client: clientHandlers){
            if(client.clientUserName.equals(trueUser)){
                client.closeEverything();
            }
        }
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
