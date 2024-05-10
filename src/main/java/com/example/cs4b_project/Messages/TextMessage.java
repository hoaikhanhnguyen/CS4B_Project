package com.example.cs4b_project.Messages;

/*
 * class TextMessage extends Message
 ******************************************************************************
 * Used to send text messages from the server to the client.
 * Text is displayed on a dialogue box in the client game board
 ******************************************************************************
 */
public class TextMessage extends Message {

    private String message;
    public TextMessage(String message) {
        super("TEXT");
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return "TextMessage{message = " + message + "}";
    }
}
