package com.example.cs4b_project.Messages;

/*
 * class StatusMessage extends Message
 ******************************************************************************
 * Used by the server to communicate the status of the client:
 *  - waiting for a move (StatusMessage.WAITING)
 *  - ready to make a move (StatusMessage.MAKE_MOVE)
 ******************************************************************************
 */
public class StatusMessage extends Message {

    private int status;
    public final int WAITING = 0;
    public final int MAKE_MOVE = 0;
    public StatusMessage(int status) {
        super("STATUS");
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
