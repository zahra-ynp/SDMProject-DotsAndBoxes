package it.units.sdm.dotsandboxes.logic;

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
}

