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
}