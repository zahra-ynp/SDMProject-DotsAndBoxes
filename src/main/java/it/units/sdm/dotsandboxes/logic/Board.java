package it.units.sdm.dotsandboxes.logic;
import it.units.sdm.dotsandboxes.model.Line;

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

    //ToDo
    private int countCompletedBoxes(Line line) {
        return 0;
    }
}