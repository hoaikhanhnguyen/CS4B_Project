package com.example.cs4b_project.Messages;

import java.io.Serializable;

public class Message implements Serializable {
    private final String type;

    public Message(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
