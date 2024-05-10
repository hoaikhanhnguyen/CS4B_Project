package com.example.cs4b_project.Messages;

/*
 * class MoveMessage extends Message
 ******************************************************************************
 * UNUSED
 * Used to communicate that a client would like to make a move on their
 * current gameboard at `pos`.
 ******************************************************************************
 */
public class MoveMessage extends Message {
    private int pos;

    public MoveMessage(int pos)
    {
        super("MOVE");
        this.pos = pos;
    }

    public int getPosition()
    {
        return pos;
    }

}
