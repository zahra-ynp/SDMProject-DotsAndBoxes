package it.units.sdm.dotsandboxes;

import it.units.sdm.dotsandboxes.logic.GameSession;
import it.units.sdm.dotsandboxes.view.ConsoleUI;

public class Main {
    public static void main(String[] args) {

        GameSession session = new GameSession(3, 3);
        ConsoleUI ui = new ConsoleUI(session);
        ui.start();
    }
}