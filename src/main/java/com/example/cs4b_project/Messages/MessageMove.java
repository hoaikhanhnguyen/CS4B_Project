package com.example.cs4b_project.Messages;

public class MessageMove extends Message {
    private int pos;

    public MessageMove(int pos)
    {
        super("MOVE");
        this.pos = pos;
    }

    public int getPosition()
    {
        return pos;
    }

}
