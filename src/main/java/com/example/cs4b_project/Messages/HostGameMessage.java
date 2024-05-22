package com.example.cs4b_project.Messages;

import java.io.Serializable;
/*
 * class HostGameMessage extends Message
 ******************************************************************************
 * UNUSED
 * Used to communicate to the server that the client would like to host
 * a game.
 ******************************************************************************
 */
public class HostGameMessage extends Message {
    public HostGameMessage()
    {
        super("HOST GAME");
    }

    @Override
    public String toString()
    {
        return "HostGameMessage{}";
    }
}
