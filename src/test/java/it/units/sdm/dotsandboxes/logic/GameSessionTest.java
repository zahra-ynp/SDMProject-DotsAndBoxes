package it.units.sdm.dotsandboxes.logic;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.units.sdm.dotsandboxes.model.Direction;
import it.units.sdm.dotsandboxes.model.Move;
import it.units.sdm.dotsandboxes.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameSessionTest {

    @Test
    void startsWithPlayerOne() {
        GameSession session = new GameSession(5, 5);
        assertEquals(Player.Player1, session.getCurrentPlayer());
    }

    @Test
    void switchesPlayerWhenNoBoxIsCompleted() {
        GameSession session = new GameSession(5, 5);

        // a simple move that cannot complete a box as first move
        session.makeMove(new Move(0, 0, Direction.HORIZONTAL));

        assertEquals(Player.Player2, session.getCurrentPlayer());
    }

    @Test
    void keepsSamePlayerWhenBoxIsCompleted() {
         GameSession session = new GameSession(2, 2);

        // P1
        session.makeMove(new Move(0, 0, Direction.HORIZONTAL)); // top

        // P2
        session.makeMove(new Move(0, 0, Direction.VERTICAL));   // left

        // P1
        session.makeMove(new Move(1, 0, Direction.HORIZONTAL)); // bottom

        // Now it's P2's turn.
        assertEquals(Player.Player2, session.getCurrentPlayer());

       // P2 plays the last edge that completes the box
       session.makeMove(new Move(0, 1, Direction.VERTICAL));   // right -> completes box

       // Because P2 completed a box, P2 should play again (no switch)
       assertEquals(Player.Player2, session.getCurrentPlayer());
    }

    @Test
    void playerStartsWithZeroScore() {
         GameSession session = new GameSession(2, 2);
         
         assertEquals(0, session.getScore(Player.Player1));
         
         assertEquals(0, session.getScore(Player.Player2));
    }

    @Test
    void scoringAddsPointsToPlayerWhoCompletedTheBox() {
         GameSession session = new GameSession(2, 2);

        // P1
        session.makeMove(new Move(0, 0, Direction.HORIZONTAL)); // top
        // P2
        session.makeMove(new Move(0, 0, Direction.VERTICAL));   // left
        // P1
        session.makeMove(new Move(1, 0, Direction.HORIZONTAL)); // bottom
        // P2 completes the box (gets the point)
        session.makeMove(new Move(0, 1, Direction.VERTICAL));   // right

        assertEquals(0, session.getScore(Player.Player1));
        assertEquals(1, session.getScore(Player.Player2));
    }

    @Test
    void gameIsOverWhenAllBoxesAreClaimed() {
         GameSession session = new GameSession(2, 2); // total boxes = 1

        // complete the single box
        session.makeMove(new Move(0, 0, Direction.HORIZONTAL)); // top
        session.makeMove(new Move(0, 0, Direction.VERTICAL));   // left
        session.makeMove(new Move(1, 0, Direction.HORIZONTAL)); // bottom
        session.makeMove(new Move(0, 1, Direction.VERTICAL));   // right (closes box)

        // now score sum should be 1, so game over
        assertTrue(session.isGameOver());
    }

    @Test
    void returnsNullWhenGameIsNotOver() {
         GameSession session = new GameSession(2, 2);
         assertNull(session.getWinner());
    }

    @Test
    void returnsWinnerWhenGameEnds() {
         GameSession session = new GameSession(2, 2);

        // Make Player1 win the only box on a 2x2 board
        session.makeMove(new Move(0, 0, Direction.HORIZONTAL)); // P1
        session.makeMove(new Move(0, 0, Direction.VERTICAL));   // P2
        session.makeMove(new Move(1, 0, Direction.HORIZONTAL)); // P1
        session.makeMove(new Move(0, 1, Direction.VERTICAL));   // P2 completes box -> score P2 = 1

        // For this sequence, Player2 wins
        assertEquals(Player.Player2, session.getWinner());
    }


}

