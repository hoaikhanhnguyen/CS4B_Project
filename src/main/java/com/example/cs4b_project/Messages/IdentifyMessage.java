package com.example.cs4b_project.Messages;

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
