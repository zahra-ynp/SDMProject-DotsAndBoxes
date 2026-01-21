package it.units.sdm.dotsandboxes.logic;
import it.units.sdm.dotsandboxes.model.Line;
import it.units.sdm.dotsandboxes.model.Player;
import it.units.sdm.dotsandboxes.model.Point;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private final int width;
    private final int height;

    // Map line -> Player (Color)
    private final Map<Line, Player> drawnLines = new HashMap<>();

    // Map Top-Left Point of a box -> Player (Winner of that box)
    private final Map<Point, Player> completedBoxes = new HashMap<>();

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
    }

    // Returns the number of boxes completed by this move (0, 1, or 2)
    // 2 is for the case that new line is between to boxes
    // Now accepts the Player who made the move
    public int addLine(Line line, Player player) {
        if (isOutOfBounds(line)) {
            throw new IllegalArgumentException("Line is outside the board boundaries");
        }
        if (drawnLines.containsKey(line)) {
            throw new IllegalArgumentException("Line already exists");
        }
        // Store the line with the player who drew it
        drawnLines.put(line, player);

        return countCompletedBoxes(line, player);
    }

    private int countCompletedBoxes(Line line, Player player) {
        int completedBoxes = 0;
        Point p1 = line.p1();
        Point p2 = line.p2();
        boolean isHorizontal = p1.row() == p2.row();

        //check for completing two boxes
        //if direction is horizontal we should check above and below boxes from player's point (p1)
        //vertical -> left and right of the p1
        if (isHorizontal) {
            // Check Box Above (Row - 1)
            if (checkAndClaimBox(p1.row() - 1, p1.col(), player)) completedBoxes++;
            // Check Box Below (Row)
            if (checkAndClaimBox(p1.row(), p1.col(), player)) completedBoxes++;
        } else {
            // Check Box Left (Col - 1)
            if (checkAndClaimBox(p1.row(), p1.col() - 1, player)) completedBoxes++;
            // Check Box Right (Col)
            if (checkAndClaimBox(p1.row(), p1.col(), player)) completedBoxes++;
        }
        return completedBoxes;
    }

    private boolean checkAndClaimBox(int row, int col, Player player) {
        // Boundary Check
        if (row < 0 || col < 0 || row >= height - 1 || col >= width - 1) {
            return false;
        }

        Line top = new Line(new Point(row, col), new Point(row, col + 1));
        Line bottom = new Line(new Point(row + 1, col), new Point(row + 1, col + 1));
        Line left = new Line(new Point(row, col), new Point(row + 1, col));
        Line right = new Line(new Point(row, col + 1), new Point(row + 1, col + 1));

        // Check if all 4 lines exist
        if (drawnLines.containsKey(top) && drawnLines.containsKey(bottom) &&
                drawnLines.containsKey(left) && drawnLines.containsKey(right)) {

            // Register the box owner!
            completedBoxes.put(new Point(row, col), player);
            return true;
        }
        return false;
    }

    // --- Getters for the UI ---

    public boolean hasLine(Line line) {
        return drawnLines.containsKey(line);
    }

    public Player getLineOwner(Line line) {
        return drawnLines.get(line);
    }

    public Player getBoxOwner(Point topLeft) {
        return completedBoxes.get(topLeft);
    }

    private boolean isOutOfBounds(Line line) {
        Point p1 = line.p1();
        Point p2 = line.p2();

        // Check columns (Width) and rows (Height)
        boolean p1Invalid = p1.col() < 0 || p1.col() >= width || p1.row() < 0 || p1.row() >= height;
        boolean p2Invalid = p2.col() < 0 || p2.col() >= width || p2.row() < 0 || p2.row() >= height;

        return p1Invalid || p2Invalid;
    }
}