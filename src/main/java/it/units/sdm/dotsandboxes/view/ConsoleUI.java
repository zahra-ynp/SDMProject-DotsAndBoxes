package it.units.sdm.dotsandboxes.view;

import it.units.sdm.dotsandboxes.logic.GameSession;
import it.units.sdm.dotsandboxes.model.Direction;
import it.units.sdm.dotsandboxes.model.Line;
import it.units.sdm.dotsandboxes.model.Move;
import it.units.sdm.dotsandboxes.model.Point;

import java.util.Scanner;

public class ConsoleUI {

    private final GameSession gameSession;
    private final Scanner scanner;

    public ConsoleUI(GameSession gameSession) {
        this.gameSession = gameSession;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("================================");
        System.out.println("   WELCOME TO DOTS AND BOXES    ");
        System.out.println("================================");

        while (!gameSession.isGameOver()) {
            printBoard();
            System.out.println("--------------------------------");
            System.out.println("Current Player: " + gameSession.getCurrentPlayer());
            System.out.println("Score: P1=" + gameSession.getScore(it.units.sdm.dotsandboxes.model.Player.Player1) +
                    " | P2=" + gameSession.getScore(it.units.sdm.dotsandboxes.model.Player.Player2));

            Move move = getValidMoveFromUser();

            try {
                gameSession.makeMove(move);
            } catch (IllegalArgumentException e) {
                // This catches "Line already exists" AND "Line is out of bounds"
                System.out.println("Invalid Move: " + e.getMessage());
                // The loop continues, asking for input again
            }
        }

        // Game Over Screen
        printBoard();
        System.out.println("================================");
        System.out.println("           GAME OVER            ");
        System.out.println("================================");

        if (gameSession.getWinner() == null) {
            System.out.println("It's a Tie!");
        } else {
            System.out.println("WINNER: " + gameSession.getWinner());
        }
    }


    private Move getValidMoveFromUser() {
        while (true) {
            System.out.println("Enter move (row col direction[H/V]): ");
            // Example input: "0 0 H" or "1 2 V"

            try {
                String input = scanner.nextLine();
                String[] parts = input.trim().split("\\s+");

                if (parts.length != 3) {
                    System.out.println("Invalid format! Use: row col H/V");
                    continue;
                }

                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                String dirChar = parts[2].toUpperCase();

                Direction direction;
                if (dirChar.equals("H")) direction = Direction.HORIZONTAL;
                else if (dirChar.equals("V")) direction = Direction.VERTICAL;
                else {
                    System.out.println("Invalid direction! Use H for Horizontal or V for Vertical.");
                    continue;
                }

                return new Move(row, col, direction);

            } catch (NumberFormatException e) {
                System.out.println("Invalid numbers! Please enter integers for row and col.");
            } catch (Exception e) {
                System.out.println("Error reading input. Try again.");
            }
        }
    }

    private void printBoard() {
        int width = gameSession.getWidth();
        int height = gameSession.getHeight();

        // Loop through each row of dots
        for (int r = 0; r < height; r++) {

            // 1. Print Horizontal Lines (Row of dots)
            for (int c = 0; c < width; c++) {
                System.out.print("."); // The Dot

                // Check if there is a horizontal line to the right
                if (c < width - 1) {
                    Line rightLine = new Line(new Point(r, c), new Point(r, c + 1));
                    if (gameSession.isLineDrawn(rightLine)) {
                        System.out.print("---");
                    } else {
                        System.out.print("   "); // Empty space
                    }
                }
            }
            System.out.println(); // New line after row of dots

            // 2. Print Vertical Lines (Between rows of dots)
            if (r < height - 1) {
                for (int c = 0; c < width; c++) {
                    Line downLine = new Line(new Point(r, c), new Point(r + 1, c));
                    if (gameSession.isLineDrawn(downLine)) {
                        System.out.print("|");
                    } else {
                        System.out.print(" ");
                    }

                    // Space between vertical struts
                    if (c < width - 1) {
                        System.out.print("   ");
                    }
                }
                System.out.println(); // New line after row of vertical lines
            }
        }
    }

}