package it.units.sdm.dotsandboxes.logic;

import it.units.sdm.dotsandboxes.model.Line;
import it.units.sdm.dotsandboxes.model.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void testAddLineSuccessfully() {
        // 1. Arrange
        Board board = new Board(3, 3);
        Line line = new Line(new Point(0, 0), new Point(0, 1));

        // 2. Act
        board.addLine(line);

        // 3. Assert
        assertTrue(board.hasLine(line), "Board should verify that the line exists");
    }

    @Test
    void testAddDuplicateLineThrowsException() {
        Board board = new Board(3, 3);
        Line line = new Line(new Point(0, 0), new Point(0, 1));

        board.addLine(line);

        // as we handled this in the board it will be pass
        assertThrows(IllegalArgumentException.class, () -> board.addLine(line));
    }

    @Test
    void testAddLineCompletesBox() {
        Board board = new Board(3, 3);

        // 1. Draw the first 3 lines of a box at (0,0)
        // Top
        board.addLine(new Line(new Point(0, 0), new Point(0, 1)));
        // Bottom
        board.addLine(new Line(new Point(1, 0), new Point(1, 1)));
        // Left
        board.addLine(new Line(new Point(0, 0), new Point(1, 0)));

        // 2. Draw the 4th line (Right side)
        Line closingLine = new Line(new Point(0, 1), new Point(1, 1));

        // 3. Act: Add the line
        int completedBoxes = board.addLine(closingLine);

        // 4. Assert: We expect exactly 1 box to be verified
        assertEquals(1, completedBoxes, "Closing a square should return 1 point");
    }

    @Test
    void testAddLineCompletesTwoBoxes() {
        Board board = new Board(3, 3);

        // 1. Set up Box 1 (Left of the middle vertical line)
        board.addLine(new Line(new Point(0, 0), new Point(0, 1))); // Top
        board.addLine(new Line(new Point(1, 0), new Point(1, 1))); // Bottom
        board.addLine(new Line(new Point(0, 0), new Point(1, 0))); // Left

        // 2. Set up Box 2 (Right of the middle vertical line)
        board.addLine(new Line(new Point(0, 1), new Point(0, 2))); // Top
        board.addLine(new Line(new Point(1, 1), new Point(1, 2))); // Bottom
        board.addLine(new Line(new Point(0, 2), new Point(1, 2))); // Right

        // 3. Act: Add the shared center vertical line
        Line sharedLine = new Line(new Point(0, 1), new Point(1, 1));
        int completedBoxes = board.addLine(sharedLine);

        // 4. Assert: Expect 2 points
        assertEquals(2, completedBoxes, "Closing a shared side should return 2 points");
    }
}


