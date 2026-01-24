package it.units.sdm.dotsandboxes;

import it.units.sdm.dotsandboxes.logic.GameSession;
import it.units.sdm.dotsandboxes.model.Direction;
import it.units.sdm.dotsandboxes.model.Move;
import it.units.sdm.dotsandboxes.view.ConsoleUI;
import it.units.sdm.dotsandboxes.view.SwingUI;

import javax.swing.*;
import java.util.Scanner;

public class Main {
     void main() {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("--- SETUP ---");
//        System.out.println("Enter number between 2 and 20 for columns (width): ");
//        int width = getValidInt(scanner);
//
//        System.out.println("Enter number between 2 and 20 for rows (height): ");
//        int height = getValidInt(scanner);
//
//        GameSession session = new GameSession(width, height);
//
//        ConsoleUI ui = new ConsoleUI(session);
//
//        ui.start();

         // 1. Setup Logic
         // Hardcode size for now to test GUI layout
         GameSession session = new GameSession(4, 4);

         // 2. Setup View (Swing)
         SwingUtilities.invokeLater(() -> {
             JFrame frame = new JFrame("Dots and Boxes");
             frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

             SwingUI ui = new SwingUI(session);
             frame.add(ui);

             frame.pack(); // Adjust window size to fit the panel
             frame.setLocationRelativeTo(null); // Center on screen
             frame.setVisible(true);
         });

     }


    private static int getValidInt(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine();
                int val = Integer.parseInt(input);

                // Check Range: Must be at least 2, but no more than 5
                if (val < 2 || val > 20) {
                    System.out.println("Invalid size! Please enter a number between 2 and 20:");
                    continue;
                }

                return val;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter an integer number:");
            }
        }
    }
}