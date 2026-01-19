package it.units.sdm.dotsandboxes.logic;

import it.units.sdm.dotsandboxes.model.Move;
import it.units.sdm.dotsandboxes.model.Player;

import java.util.EnumMap;
import java.util.Map;

public class GameSession {

    private final Board board;
    private Player currentPlayer;

    // Store scores per player (EnumMap is perfect for enums)
    private final Map<Player, Integer> scores;

    public GameSession(int width, int height) {
        // Player always starts
        this.board = new Board(width, height);
        this.currentPlayer = Player.Player1;

        // Initialize scores to 0 for both players
        scores = new EnumMap<>(Player.class);
        scores.put(Player.Player1, 0);
        scores.put(Player.Player2, 0);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getScore(Player player) {
        return scores.get(player);
    }

    public void makeMove(Move move) {
        // Convert the user's move into a concrete line on the board
        // Board will validate duplicates and will also tell us if the move completes boxes.
        int completedBoxes = board.addLine(move.toLine());

        // If no box was completed -> switch player.
        // If one (or two) boxes were completed -> player gets an extra turn (do not switch).
        if (completedBoxes == 0) {
            // Minimal rule: switch player when the move does not score
            currentPlayer = (currentPlayer == Player.Player1) ? Player.Player2 : Player.Player1;
        } else {
            // Add points to the current player for each completed box
            scores.put(currentPlayer, scores.get(currentPlayer) + completedBoxes);
            // player gets an extra turn (do not switch)
        }
    }
}
