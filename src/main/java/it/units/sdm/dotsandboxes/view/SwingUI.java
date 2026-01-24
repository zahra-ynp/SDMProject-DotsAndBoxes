package it.units.sdm.dotsandboxes.view;

import it.units.sdm.dotsandboxes.logic.GameSession;
import it.units.sdm.dotsandboxes.model.Direction;
import it.units.sdm.dotsandboxes.model.Line;
import it.units.sdm.dotsandboxes.model.Move;
import it.units.sdm.dotsandboxes.model.Player;
import it.units.sdm.dotsandboxes.model.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class SwingUI extends JPanel {

    private final GameSession session;

    // Track the "Ghost" move (the line under the mouse)
    private Move currentHoverMove = null;

    // Visual settings
    private static final int DOT_SIZE = 10;
    private static final int LINE_THICKNESS = 6;
    private static final int SPACING = 60;
    private static final int OFFSET = 50;
    private static final int HITBOX_WIDTH = 20; // How "fat" the invisible click area is

    public SwingUI(GameSession session) {
        this.session = session;
        this.setPreferredSize(new Dimension(400, 400));
        this.setBackground(Color.WHITE);

        // 1. Mouse MOVED (For Hover Effect)
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                handleMouseMove(e.getX(), e.getY());
            }
        });

        // 2. Mouse CLICKED (For Action)
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentHoverMove != null) {
                    // We simply execute the move we are already hovering over!
                    try {
                        session.makeMove(currentHoverMove);
                        currentHoverMove = null; // Clear hover after move
                        repaint();
                        checkGameOver();
                    } catch (IllegalArgumentException ex) {
                        // Ignore
                    }
                }
            }
        });
    }

    // Logic to calculate which line is closest to the mouse
    private void handleMouseMove(int mouseX, int mouseY) {
        Move bestCandidate = null;

        // Reset Logic
        // We scan all possible lines to see if the mouse is "inside" one

        for (int r = 0; r < session.getHeight(); r++) {
            for (int c = 0; c < session.getWidth(); c++) {

                // --- Check Horizontal Segment ---
                if (c < session.getWidth() - 1) {
                    int lineY = OFFSET + (r * SPACING);
                    int startX = OFFSET + (c * SPACING) + DOT_SIZE;
                    int endX = OFFSET + ((c + 1) * SPACING) - DOT_SIZE;

                    if (mouseX >= startX && mouseX <= endX &&
                            Math.abs(mouseY - lineY) < HITBOX_WIDTH / 2) {

                        // Check if line is already taken
                        Line l = new Line(new Point(r, c), new Point(r, c+1));
                        if (!session.isLineDrawn(l)) {
                            bestCandidate = new Move(r, c, Direction.HORIZONTAL);
                        }
                    }
                }

                // --- Check Vertical Segment ---
                if (r < session.getHeight() - 1) {
                    int lineX = OFFSET + (c * SPACING);
                    int startY = OFFSET + (r * SPACING) + DOT_SIZE;
                    int endY = OFFSET + ((r + 1) * SPACING) - DOT_SIZE;

                    if (mouseY >= startY && mouseY <= endY &&
                            Math.abs(mouseX - lineX) < HITBOX_WIDTH / 2) {

                        Line l = new Line(new Point(r, c), new Point(r+1, c));
                        if (!session.isLineDrawn(l)) {
                            bestCandidate = new Move(r, c, Direction.VERTICAL);
                        }
                    }
                }
            }
        }

        // Only repaint if the hover state changed
        if ((bestCandidate == null && currentHoverMove != null) ||
                (bestCandidate != null && !bestCandidate.equals(currentHoverMove))) {
            currentHoverMove = bestCandidate;
            repaint();
        }
    }

    private void checkGameOver() {
        if (session.isGameOver()) {
            JOptionPane.showMessageDialog(this,
                    "Game Over! Winner: " + (session.getWinner() == null ? "Draw" : session.getWinner().name()));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawLines(g2d);
        drawDots(g2d);
        drawHover(g2d);
        drawScores(g2d);
    }

    private void drawHover(Graphics2D g2d) {
        if (currentHoverMove != null) {
            g2d.setColor(new Color(200, 200, 200)); // Light Gray
            g2d.setStroke(new BasicStroke(LINE_THICKNESS));

            int r = currentHoverMove.row();
            int c = currentHoverMove.col();

            if (currentHoverMove.direction() == Direction.HORIZONTAL) {
                int x1 = OFFSET + (c * SPACING);
                int y1 = OFFSET + (r * SPACING);
                int x2 = OFFSET + ((c + 1) * SPACING);
                g2d.drawLine(x1, y1, x2, y1);
            } else {
                int x1 = OFFSET + (c * SPACING);
                int y1 = OFFSET + (r * SPACING);
                int y2 = OFFSET + ((r + 1) * SPACING);
                g2d.drawLine(x1, y1, x1, y2);
            }
        }
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
    }

    private Color getPlayerColor(Player player) {
        if (player == Player.Player1) return Color.BLUE;
        if (player == Player.Player2) return Color.RED;
        return Color.BLACK;
    }
}