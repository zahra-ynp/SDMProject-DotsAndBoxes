package it.units.sdm.dotsandboxes;

import it.units.sdm.dotsandboxes.logic.GameSession;
import it.units.sdm.dotsandboxes.view.ConsoleUI;
import java.util.Scanner;

public class Main {
     void main() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- SETUP ---");
        System.out.println("Enter number between 2 and 5 for columns (width): ");
        int width = getValidInt(scanner);

        System.out.println("Enter number between 2 and 5 for rows (height): ");
        int height = getValidInt(scanner);

        GameSession session = new GameSession(width, height);

        ConsoleUI ui = new ConsoleUI(session);

        ui.start();
    }

    private static int getValidInt(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine();
                int val = Integer.parseInt(input);

                // Check Range: Must be at least 2, but no more than 5
                if (val < 2 || val > 5) {
                    System.out.println("Invalid size! Please enter a number between 2 and 5:");
                    continue;
                }

                return val;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter an integer number:");
            }
        }
    }
}