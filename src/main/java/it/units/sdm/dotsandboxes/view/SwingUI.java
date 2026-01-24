package it.units.sdm.dotsandboxes.view;

import it.units.sdm.dotsandboxes.logic.GameSession;
import it.units.sdm.dotsandboxes.model.Line;
import it.units.sdm.dotsandboxes.model.Player;
import it.units.sdm.dotsandboxes.model.Point;

import javax.swing.*;
import java.awt.*;

public class SwingUI extends JPanel {

    private final GameSession session;

    // Visual settings
    private static final int DOT_SIZE = 10;
    private static final int LINE_THICKNESS = 4;
    private static final int SPACING = 60; // Pixels between dots
    private static final int OFFSET = 50;  // Margin from window edge

    public SwingUI(GameSession session) {
        this.session = session;
        this.setPreferredSize(new Dimension(400, 400)); // Default window size
        this.setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawLines(g2d);
        drawDots(g2d);
        drawScores(g2d);
    }

    private void drawDots(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        for (int row = 0; row < session.getHeight(); row++) {
            for (int col = 0; col < session.getWidth(); col++) {
                int x = OFFSET + (col * SPACING);
                int y = OFFSET + (row * SPACING);
                g2d.fillOval(x - DOT_SIZE / 2, y - DOT_SIZE / 2, DOT_SIZE, DOT_SIZE);
            }
        }
    }

    private void drawLines(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(LINE_THICKNESS));

        // We iterate over ALL logical connections to see if they are drawn
        for (int r = 0; r < session.getHeight(); r++) {
            for (int c = 0; c < session.getWidth(); c++) {

                // Check Horizontal Line (Right)
                if (c < session.getWidth() - 1) {
                    Line line = new Line(new Point(r, c), new Point(r, c + 1));
                    if (session.isLineDrawn(line)) {
                        g2d.setColor(getPlayerColor(session.getLineOwner(line)));
                        int x1 = OFFSET + (c * SPACING);
                        int y1 = OFFSET + (r * SPACING);
                        int x2 = OFFSET + ((c + 1) * SPACING);
                        g2d.drawLine(x1, y1, x2, y1);
                    }
                }

                // Check Vertical Line (Down)
                if (r < session.getHeight() - 1) {
                    Line line = new Line(new Point(r, c), new Point(r + 1, c));
                    if (session.isLineDrawn(line)) {
                        g2d.setColor(getPlayerColor(session.getLineOwner(line)));
                        int x1 = OFFSET + (c * SPACING);
                        int y1 = OFFSET + (r * SPACING);
                        int y2 = OFFSET + ((r + 1) * SPACING);
                        g2d.drawLine(x1, y1, x1, y2);
                    }
                }
            }
        }
    }

    private void drawScores(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String scores = String.format("P1 (Blue): %d   |   P2 (Red): %d",
                session.getScore(Player.Player1), session.getScore(Player.Player2));
        g2d.drawString(scores, OFFSET, OFFSET / 2);

        // Show winner if game over
        if (session.isGameOver()) {
            g2d.setColor(new Color(0, 128, 0));
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            String msg = "GAME OVER! Winner: " + (session.getWinner() == null ? "Draw" : session.getWinner().name());
            g2d.drawString(msg, OFFSET, getHeight() - 20);
        }
    }

    private Color getPlayerColor(Player player) {
        if (player == Player.Player1) return Color.BLUE;
        if (player == Player.Player2) return Color.RED;
        return Color.BLACK;
    }
}