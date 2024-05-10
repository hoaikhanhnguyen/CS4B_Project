package com.example.cs4b_project.Messages;

/*
 * class IdentifyMessage extends Message
 ******************************************************************************
 * UNUSED
 * Used to communicate to the server the self-assigned ID of a client.
 ******************************************************************************
 */
public class IdentifyMessage extends Message {
    private String ID;
    public IdentifyMessage(String ID) {
        super("IDENTIFY");
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }
}
