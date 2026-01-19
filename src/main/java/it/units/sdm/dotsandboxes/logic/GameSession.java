package it.units.sdm.dotsandboxes.logic;

import it.units.sdm.dotsandboxes.model.Player;

public class GameSession {

    private Player currentPlayer;

    public GameSession(int width, int height) {
        // Player always starts
        this.currentPlayer = Player.Player1;

    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}

