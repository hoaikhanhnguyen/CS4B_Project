package com.example.cs4b_project;

public class GameInstance {
    private Game game;
    private String player1;
    private String player2;

    public GameInstance() {
        game = new Game();
    }

    // This just stores information about the game. Any actual messaging is done on the server side.

    public Game getGame() {
        return game;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }
}
