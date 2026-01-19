package it.units.sdm.dotsandboxes.logic;

import it.units.sdm.dotsandboxes.model.Move;
import it.units.sdm.dotsandboxes.model.Player;

public class GameSession {

    private final Board board;
    private Player currentPlayer;

    public GameSession(int width, int height) {
        // Player always starts
        this.board = new Board(width, height);
        this.currentPlayer = Player.Player1;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
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
        }
    }
}
