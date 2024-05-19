package com.example.cs4b_project.Messages;

/*
 * class TextMessage extends Message
 ******************************************************************************
 * Used to send text messages from the server to the client.
 * Text is displayed on a dialogue box in the client game board
 ******************************************************************************
 */
public class ErrorMessage extends Message {

    private int player;
    public static final int PLAYER_1 = 1;
    public static final int PLAYER_2 = 2;

    private String message;
    public ErrorMessage(int player) {
        super("ERROR");
        this.player = player;
    }

    public int getPlayer() {return player; }
}
