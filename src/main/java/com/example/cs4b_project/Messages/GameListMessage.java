package com.example.cs4b_project.Messages;

import com.example.cs4b_project.GameInstance;

import java.util.ArrayList;
import java.util.Collections;

public class GameListMessage extends Message {
    private ArrayList<GameInstance> gameInstances;
    public GameListMessage(ArrayList<GameInstance> gameInstances) {
        super("GAMELIST");
        this.gameInstances = new ArrayList<>();
        gameInstances.ensureCapacity(gameInstances.size());
        Collections.copy(this.gameInstances, gameInstances);
    }

    public ArrayList<GameInstance> getGameInstances() {
        return gameInstances;
    }
}
