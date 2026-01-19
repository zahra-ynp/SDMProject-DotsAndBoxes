package it.units.sdm.dotsandboxes.logic;

import it.units.sdm.dotsandboxes.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameSessionTest {

    @Test
    void gameStartsWithPlayerOne() {
        GameSession session = new GameSession(5, 5);
        assertEquals(Player.PLAYER_ONE, session.getCurrentPlayer());
    }
}
