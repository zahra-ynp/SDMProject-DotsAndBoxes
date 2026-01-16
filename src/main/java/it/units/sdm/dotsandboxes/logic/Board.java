package it.units.sdm.dotsandboxes.logic;
import it.units.sdm.dotsandboxes.model.Line;
import it.units.sdm.dotsandboxes.model.Point;

import java.util.HashSet;
import java.util.Set;

public class Board {
    private final int width;
    private final int height;
    private final Set<Line> drawnLines;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        //with using HashSet we can check presence of line easily
        this.drawnLines = new HashSet<>();
    }

    // Returns the number of boxes completed by this move (0, 1, or 2)
    // 2 is for the case that new line is between to boxes
    public int addLine(Line line) {
        if (drawnLines.contains(line)) {
            throw new IllegalArgumentException("Line already exists");
        }
        drawnLines.add(line);

        return countCompletedBoxes(line);
    }

    public boolean hasLine(Line line) {
        return drawnLines.contains(line);
    }


    private int countCompletedBoxes(Line line) {
        int completedBoxes = 0;
        Point p1 = line.p1();
        Point p2 = line.p2();

        boolean isHorizontal = p1.row() == p2.row();

        //check for completing two boxes
        //if direction is horizontal we should check above and below boxes from player's point (p1)
        //vertical -> left and right of the p1
        if (isHorizontal) {
            // Check Box Above (Row - 1)
            if (isBoxComplete(p1.row() - 1, p1.col())) completedBoxes++;
            // Check Box Below (Row)
            if (isBoxComplete(p1.row(), p1.col())) completedBoxes++;
        } else {
            // Check Box Left (Col - 1)
            if (isBoxComplete(p1.row(), p1.col() - 1)) completedBoxes++;
            // Check Box Right (Col)
            if (isBoxComplete(p1.row(), p1.col())) completedBoxes++;
        }
        return completedBoxes;
    }

    private boolean isBoxComplete(int row, int col) {
        // Check boundaries
        if (row < 0 || col < 0 || row >= height - 1 || col >= width - 1) {
            return false;
        }

        // Define the 4 sides of the box
        Line top    = new Line(new Point(row, col),     new Point(row, col + 1));
        Line bottom = new Line(new Point(row + 1, col), new Point(row + 1, col + 1));
        Line left   = new Line(new Point(row, col),     new Point(row + 1, col));
        Line right  = new Line(new Point(row, col + 1), new Point(row + 1, col + 1));

        // Check if all 4 exist
        return drawnLines.contains(top) &&
                drawnLines.contains(bottom) &&
                drawnLines.contains(left) &&
                drawnLines.contains(right);
    }
}