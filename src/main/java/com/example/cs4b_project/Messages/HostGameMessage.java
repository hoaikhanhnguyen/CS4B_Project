package com.example.cs4b_project.Messages;

import java.io.Serializable;
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
