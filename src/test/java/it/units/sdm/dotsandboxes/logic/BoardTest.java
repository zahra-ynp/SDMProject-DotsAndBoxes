package it.units.sdm.dotsandboxes.logic;

import it.units.sdm.dotsandboxes.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void testAddLineSuccessfully() {
        // 1. Arrange
        Board board = new Board(3, 3);
        Line line = new Line(new Point(0, 0), new Point(0, 1));

        // 2. Act
        board.addLine(line, Player.Player1);

        // 3. Assert
        assertTrue(board.hasLine(line), "Board should verify that the line exists");
    }

    @Test
    void testAddDuplicateLineThrowsException() {
        Board board = new Board(3, 3);
        Line line = new Line(new Point(0, 0), new Point(0, 1));

        board.addLine(line,Player.Player1);

        // as we handled this in the board it will be pass
        assertThrows(IllegalArgumentException.class, () -> board.addLine(line,Player.Player1));
    }

    @Test
    void testAddLineCompletesBox() {
        Board board = new Board(3, 3);

        // 1. Draw the first 3 lines of a box at (0,0)
        // Top
        board.addLine(new Line(new Point(0, 0), new Point(0, 1)),Player.Player1);
        // Bottom
        board.addLine(new Line(new Point(1, 0), new Point(1, 1)),Player.Player2);
        // Left
        board.addLine(new Line(new Point(0, 0), new Point(1, 0)),Player.Player1);

        // 2. Draw the 4th line (Right side)
        Line closingLine = new Line(new Point(0, 1), new Point(1, 1));

        // 3. Act: Add the line
        int completedBoxes = board.addLine(closingLine,Player.Player2);

        // 4. Assert: We expect exactly 1 box to be verified
        assertEquals(1, completedBoxes, "Closing a square should return 1 point");
    }

    @Test
    void testAddLineCompletesTwoBoxes() {
        Board board = new Board(3, 3);

        // 1. Set up Box 1 (Left of the middle vertical line)
        board.addLine(new Line(new Point(0, 0), new Point(0, 1)),Player.Player1); // Top
        board.addLine(new Line(new Point(1, 0), new Point(1, 1)),Player.Player2); // Bottom
        board.addLine(new Line(new Point(0, 0), new Point(1, 0)),Player.Player2); // Left

        // 2. Set up Box 2 (Right of the middle vertical line)
        board.addLine(new Line(new Point(0, 1), new Point(0, 2)),Player.Player1); // Top
        board.addLine(new Line(new Point(1, 1), new Point(1, 2)),Player.Player2); // Bottom
        board.addLine(new Line(new Point(0, 2), new Point(1, 2)),Player.Player1); // Right

        // 3. Act: Add the shared center vertical line
        Line sharedLine = new Line(new Point(0, 1), new Point(1, 1));
        int completedBoxes = board.addLine(sharedLine,Player.Player2);

        // 4. Assert: Expect 2 points
        assertEquals(2, completedBoxes, "Closing a shared side should return 2 points");
    }

    @Test
    void testBoxOwnerIsAssignedToThePlayerWhoClosesIt() {
        Board board = new Board(2, 2); // 1 box total
        Point p00 = new Point(0, 0);
        Point p01 = new Point(0, 1);
        Point p10 = new Point(1, 0);
        Point p11 = new Point(1, 1);

        // 1. Player 1 does all the hard work (draws 3 sides)
        board.addLine(new Line(p00, p01), Player.Player1); // Top
        board.addLine(new Line(p10, p11), Player.Player1); // Bottom
        board.addLine(new Line(p00, p10), Player.Player1); // Left

        // Verify box is not owned yet
        assertNull(board.getBoxOwner(new Point(0, 0)));

        // 2. Player 2 sneaks in and closes the box (Right side)
        board.addLine(new Line(p01, p11), Player.Player2);

        // 3. Assert: The box must belong to Player 2
        assertEquals(Player.Player2, board.getBoxOwner(new Point(0, 0)),
                "The player who completes the box should own it, even if they only drew 1 line.");
    }

    @Test
    void testLineOwnershipIs() {
        GameSession session = new GameSession(2, 2);
        Move move = new Move(0, 0, Direction.HORIZONTAL);

        session.makeMove(move);

        // Verify Player 1 owns the line
        assertEquals(Player.Player1, session.getLineOwner(move.toLine()));
    void testAddLineOutOfBoundsThrowsException() {
        Board board = new Board(3, 3);

        // 1. Define lines that are clearly outside the 3x3 grid (indices 0,1,2)
        Line negativeRow = new Line(new Point(-1, 0), new Point(0, 0));
        Line tooBigCol   = new Line(new Point(0, 3), new Point(0, 4));
        Line farAway     = new Line(new Point(10, 10), new Point(10, 11));

        // 2. Assert that adding these lines causes a crash
        assertThrows(IllegalArgumentException.class, () -> board.addLine(negativeRow));
        assertThrows(IllegalArgumentException.class, () -> board.addLine(tooBigCol));
        assertThrows(IllegalArgumentException.class, () -> board.addLine(farAway));
    }
}


