package com.example.cs4b_project;
import java.util.Arrays;

public class Game {
    private int[] board;

    private int winner = 0;

    public Game() {
        board = new int[9];
        Arrays.fill(board, 0);
    }

    public void checkWin() {
        for (int i = 0 ; i < 3 ; i++) {
            if (board[i] != 0 && board[i] == board[i+3] && board[i+3] == board[i+6]) {
                winner = board[i];
            }
            if (board[i*3] != 0 && board[i*3] == board[i*3+1] && board[i*3+1] == board[i*3+2]) {
                winner = board[i];
            }
        }
        if (board[0] != 0 && board[0] == board[4] && board[4] == board[8]) {
            winner = board[0];
        }
        if (board[2] != 0 && board[2] == board[4] && board[4] == board[6]) {
            winner = board[2];
        }
    }

    public void setPos(int where, int player) {
        this.board[where] = player;
        checkWin();
    }

    public int getPos(int where) {
        return this.board[where];
    }

    public int getWinner() {
        return winner;
    }

    public void reset() {
        for (int i = 0 ; i < 9 ; i++) {
            board[i] = 0;
        }
        winner = 0;
    }

    public void dumpBoard() {
        for (int i = 0 ; i < 9 ; i++) {
            if (i % 3 == 0) System.out.println();
            System.out.print(this.board[i] + " ");
        }
        System.out.println();
        System.out.println("Winner = " + this.getWinner());
    }
}
