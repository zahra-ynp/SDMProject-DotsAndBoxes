package it.units.sdm.dotsandboxes.logic;

import it.units.sdm.dotsandboxes.model.Move;
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

    public void makeMove(Move move) {
        // Minimal for this iteration: assume no box completed -> switch player
        currentPlayer = (currentPlayer == Player.Player1) ? Player.Player2 : Player.Player1;
    }
}
