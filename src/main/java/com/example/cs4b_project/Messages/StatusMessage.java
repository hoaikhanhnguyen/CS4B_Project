package com.example.cs4b_project.Messages;

/*
 * class StatusMessage extends Message
 ******************************************************************************
 * Used by the server to communicate the status of the client:
 *  - waiting for a move (StatusMessage.WAITING)
 *  - ready to make a move (StatusMessage.MAKE_MOVE)
 *  - finished with the game (StatusMessage.WINNER_1, StatusMessage.WINNER_2)
 * Includes current tic-tac-toe gameboard.
 ******************************************************************************
 */
public class StatusMessage extends Message {

    private int status;
    public static final int WAITING = 0;
    public static final int MAKE_MOVE = 1;
    public static final int WINNER_1 = 2;
    public static final int WINNER_2 = 3;
    public static final int WINNER_3 = 5;
    public static final int START_GAME = 4;
    public static final int RESTART = 7;
    public static final int CHANGE_TURN_1 = 8;
    public static final int CHANGE_TURN_2 = 9;
    public static final int UPDATE_COUNTER = 10;
    public static final int UPDATE_WIN_COUNTER_1 = 11;
    public static final int UPDATE_WIN_COUNTER_2 = 12;


    private int[] board = new int[9];


    public StatusMessage(int status) {
        super("STATUS");
        this.status = status;
    }

    public StatusMessage(int status, int[] board) {
        super("STATUS");
        this.status = status;
        for (int i = 0 ; i < 9 ; i++) {
            this.board[i] = board[i];
        }
    }

    public int getStatus() {
        return status;
    }

    public int[] getBoard() {
        return board;
    }
}
