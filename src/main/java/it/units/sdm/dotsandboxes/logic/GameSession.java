package it.units.sdm.dotsandboxes.logic;

import it.units.sdm.dotsandboxes.model.Line;
import it.units.sdm.dotsandboxes.model.Move;
import it.units.sdm.dotsandboxes.model.Player;
import it.units.sdm.dotsandboxes.model.Point;

import java.util.EnumMap;
import java.util.Map;

public class GameSession {

    private final Board board;
    private Player currentPlayer;

    // Keep board size here so GameSession can know when the game ends
    private final int width;
    private final int height;

    // Store scores per player (EnumMap is perfect for enums)
    private final Map<Player, Integer> scores;

    public GameSession(int width, int height) {
        // Player always starts
        this.width = width;
        this.height = height;

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
        int completedBoxes = board.addLine(move.toLine(), currentPlayer);

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

    public boolean isGameOver() {
        // Total boxes = (dots-1) * (dots-1)
        int totalBoxes = (width - 1) * (height - 1);

        // Game ends when all boxes are claimed by either player
        int claimedBoxes = scores.get(Player.Player1) + scores.get(Player.Player2);

        return claimedBoxes == totalBoxes;
    }

    public Player getWinner() {
        // If the game is not over yet, there is no winner
        if (!isGameOver()) {
            return null;
        }

        int scoreP1 = scores.get(Player.Player1);
        int scoreP2 = scores.get(Player.Player2);

        // If scores are equal when the game ends -> tie
        if (scoreP1 == scoreP2) {
            return null;
        }

        return (scoreP1 > scoreP2) ? Player.Player1 : Player.Player2;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isLineDrawn(Line line) {
        return board.hasLine(line);
    }

    public Player getLineOwner(Line line) {
        return board.getLineOwner(line);
    }

    public Player getBoxOwner(Point topLeft) {
        return board.getBoxOwner(topLeft);
    }

}
