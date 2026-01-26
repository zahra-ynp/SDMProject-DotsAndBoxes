package it.units.sdm.dotsandboxes;

import it.units.sdm.dotsandboxes.logic.GameSession;
import it.units.sdm.dotsandboxes.view.ConsoleUI;
import it.units.sdm.dotsandboxes.view.SwingUI;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class Main {
      public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("===========================================");
            System.out.println("      DOTS AND BOXES - GAME LAUNCHER       ");
            System.out.println("===========================================");
            System.out.println("Select your interface:");
            System.out.println("1. Console Version (Text-based)");
            System.out.println("2. GUI Version (Graphical Window)");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    startConsoleVersion(scanner);
                    return;
                case "2":
                    startGuiVersion();
                    return;
                case "3":
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("\n>>> Invalid choice! Please enter 1, 2, or 3. <<<\n");
            }
        }
    }

    // --- CONSOLE ---
    private static void startConsoleVersion(Scanner scanner) {
        System.out.println("\n--- CONSOLE MODE ---");

        System.out.println("Enter number between 2 and 10 for columns (width): ");
        int width = getValidInt(scanner);

        System.out.println("Enter number between 2 and 10 for rows (height): ");
        int height = getValidInt(scanner);

        GameSession session = new GameSession(width, height);
        ConsoleUI ui = new ConsoleUI(session);
        ui.start();
    }

    // --- GUI ---
    private static void startGuiVersion() {
        SwingUtilities.invokeLater(() -> {
            JFrame invisibleParent = new JFrame();
            invisibleParent.setAlwaysOnTop(true);
            invisibleParent.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            invisibleParent.setLocationRelativeTo(null);
            invisibleParent.setVisible(false);

            int width = getValidGuiSize(invisibleParent, "Enter Number for Width (Columns)", "Min 2, Max 10");
            if (width == -1) {
                invisibleParent.dispose();
                return;
            }

            int height = getValidGuiSize(invisibleParent, "Enter Number for Height (Rows)", "Min 2, Max 10");
            if (height == -1) {
                invisibleParent.dispose();
                return;
            }

            invisibleParent.dispose();

            GameSession session = new GameSession(width, height);

            JFrame frame = new JFrame("Dots and Boxes");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            SwingUI ui = new SwingUI(session);
            frame.add(ui);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.toFront();
            frame.requestFocus();
        });
    }

    // --- HELPER: Console Validation ---
    private static int getValidInt(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine();
                int val = Integer.parseInt(input);
                if (val < 2 || val > 10) {
                    System.out.println("Invalid size! Please enter a number between 2 and 10:");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter an integer number:");
            }
        }
    }

    // --- HELPER: GUI Validation ---
    private static int getValidGuiSize(JFrame parent, String dimensionName, String subtext) {
        while (true) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Color.WHITE);

            JLabel titleLabel = new JLabel("<html><div style='text-align: center;'>" +
                    "<h2 style='color: #000000;font-size: 12px; margin: 0;'>" + dimensionName + "</h2>" +
                    "<p style='color: #666666; font-size: 10px; margin-top: 5px;'>" + subtext + "</p>" +
                    "</div></html>");
            titleLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            panel.add(titleLabel);

            panel.add(Box.createVerticalStrut(10));

            JTextField textField = new JTextField(5);
            textField.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setMaximumSize(new java.awt.Dimension(100, 30));
            panel.add(textField);

            int result = JOptionPane.showConfirmDialog(
                    parent,
                    panel,
                    "Game Setup",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) return -1;

            try {
                String input = textField.getText().trim();
                int val = Integer.parseInt(input);
                if (val >= 2 && val <= 10) {
                    return val;
                } else {
                    JOptionPane.showMessageDialog(parent, "Please enter a number between 2 and 10.", "Invalid Range", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parent, "Invalid number! Please enter an integer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}