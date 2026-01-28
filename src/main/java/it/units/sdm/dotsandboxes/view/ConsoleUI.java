package it.units.sdm.dotsandboxes.view;

import it.units.sdm.dotsandboxes.logic.GameSession;
import it.units.sdm.dotsandboxes.model.Direction;
import it.units.sdm.dotsandboxes.model.Line;
import it.units.sdm.dotsandboxes.model.Move;
import it.units.sdm.dotsandboxes.model.Player;
import it.units.sdm.dotsandboxes.model.Point;

import java.util.Scanner;

public class ConsoleUI {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";

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

        printMoveHelp();

        while (!gameSession.isGameOver()) {
            printBoard();
            System.out.println("--------------------------------");
            String pName = colorize(gameSession.getCurrentPlayer().name(), gameSession.getCurrentPlayer());
            System.out.println("Current Player: " + pName);
            String p1Score = colorize("P1=" + gameSession.getScore(Player.Player1), Player.Player1);
            String p2Score = colorize("P2=" + gameSession.getScore(Player.Player2), Player.Player2);
            System.out.println("Score: " + p1Score + " | " + p2Score);

            Move move = getValidMoveFromUser();

            try {
                gameSession.makeMove(move);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Move: " + e.getMessage());
            }
        }

        // Game Over Screen
        printBoard();
        System.out.println("================================");
        System.out.println("           GAME OVER            ");
        System.out.println("================================");

        Player winner = gameSession.getWinner();
        if (winner == null) {
            System.out.println("It's a Tie!");
        } else {
            System.out.println("WINNER: " + colorize(winner.name(), winner));
        }
    }
    

    private void printMoveHelp() {
        System.out.println();
        System.out.println("How to make a move:");
        System.out.println();
        System.out.println("Type: row column direction");
        System.out.println("• row, column = position of the dot (starting from 0)");
        System.out.println("• direction:");
        System.out.println("    H = draw a horizontal line (left -> right)");
        System.out.println("    V = draw a vertical line (top -> bottom)");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("• 1 1 H -> connects dot (1,1) to dot (1,2)");
        System.out.println("• 0 2 V -> connects dot (0,2) to dot (1,2)");
        System.out.println();
        System.out.println("Type 'help' any time to see these instructions again.");
        System.out.println();
    }


    private void printQuitHelp() {
        System.out.println("You can type 'quit' or 'exit' anytime to stop the game.");
    }


    private Move getValidMoveFromUser() {
        while (true) {
            System.out.print("Enter move (row col H/V) or type 'help': ");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting game. Bye!");
                System.exit(0);
            }

            // If user types help, show instructions again
            if (input.equalsIgnoreCase("help")) {
                printMoveHelp();
                continue;
            }


            try {
                String[] parts = input.split("\\s+");
                if (parts.length != 3) throw new IllegalArgumentException("Use format: row col H/V");

                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                String dirChar = parts[2].toUpperCase();

                if (!dirChar.equals("H") && !dirChar.equals("V")) {
                    throw new IllegalArgumentException("Direction must be H or V");
                }

                Direction dir = dirChar.equals("H") ? Direction.HORIZONTAL : Direction.VERTICAL;
                return new Move(row, col, dir);

            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter numbers for row and col.");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid! " + e.getMessage());
            }
        }
    }

    private void printBoard() {
        int width = gameSession.getWidth();
        int height = gameSession.getHeight();

        // ---- Column labels ----
        System.out.print("    "); // left padding for row labels
        for (int c = 0; c < width; c++) {
            System.out.printf("%-4d", c); // each column takes 4 chars
        }
        System.out.println();
        

        // ---- Board ----
        for (int r = 0; r < height; r++) {

            // Row label
            System.out.printf("%2d  ", r);

            // Dots + horizontal lines
            for (int c = 0; c < width; c++) {
                System.out.print(".");

                if (c < width - 1) {
                    Line right = new Line(new Point(r, c), new Point(r, c + 1));
                    if (gameSession.isLineDrawn(right)) {
                        Player owner = gameSession.getLineOwner(right);
                        System.out.print(colorize("---", owner));
                    } else {
                        System.out.print("   ");
                    }
                }
            }
            System.out.println();

            // Vertical lines + box owners
            if (r < height - 1) {
                System.out.print("    ");
                for (int c = 0; c < width; c++) {
                    Line down = new Line(new Point(r, c), new Point(r + 1, c));
                    if (gameSession.isLineDrawn(down)) {
                        Player owner = gameSession.getLineOwner(down);
                        System.out.print(colorize("|", owner));
                    } else {
                        System.out.print(" ");
                    }

                    if (c < width - 1) {
                        Player boxOwner = gameSession.getBoxOwner(new Point(r, c));
                        if (boxOwner != null) {
                            String label = boxOwner == Player.Player1 ? " P1" : " P2";
                            System.out.print(colorize(label, boxOwner));
                        } else {
                            System.out.print("   ");
                        }
                    }
                }
                System.out.println();
            }
        }
    }

    private String colorize(String text, Player player) {
        if (player == null) return text;

        if (player == Player.Player1) {
            return BLUE + text + RESET;
        } else {
            return RED + text + RESET;
        }
    }
}


