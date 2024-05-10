package com.example.cs4b_project.Messages;

/*
 * class GameListMessage extends Message
 ******************************************************************************
 * UNUSED
 * Used to communicate to the server that a client is requesting the game list
 * from the server. Server should send back GameListMessage.
 ******************************************************************************
 */
public class RequestGameListMessage extends Message {

    public RequestGameListMessage() {
        super("REQUEST_GAME_LIST");
    }

}
