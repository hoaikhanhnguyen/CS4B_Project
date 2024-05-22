package com.example.cs4b_project.Messages;

/*
 * class InitGameMessage extends Message
 ******************************************************************************
 * Used by the server to communicate to the client initial information about
 * the game when the game starts.
 ******************************************************************************
 */
public class WhichPlayerMessage extends Message {
    private int player;
    public static final int PLAYER_1 = 1;
    public static final int PLAYER_2 = 2;
    public WhichPlayerMessage(int player) {
        super("WHICH_PLAYER");
        this.player = player;
    }

    public int getPlayer() {
        return player;
    }
}
