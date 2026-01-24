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
        if (!session.isGameOver()) return;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        Player winner = session.getWinner();
        String titleText;
        String colorHex;

        if (winner == null) {
            titleText = "IT'S A TIE!";
            colorHex = "333333"; // Dark Gray
        } else {
            titleText = "WINNER: " + winner.name();
            Color c = getPlayerColor(winner);
            colorHex = String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
        }

        String html = "<html><body style='width: 300px; text-align: center; font-family: sans-serif;'>" +

                // WINNER TITLE
                "<h1 style='font-size: 26px; color: " + colorHex + "; margin: 10px 0;'>" + titleText + "</h1>" +

                // CONGRATULATIONS
                "<h2 style='font-size: 18px; color: #444444; margin: 0;'>Congratulations!</h2>" +

                // SEPARATOR LINE
                "<hr style='margin: 15px 0; border: 0; border-top: 1px solid #ccc;'>" +

                // FINAL SCORE SECTION
                "<h3 style='font-size: 16px; color: #000000; margin-bottom: 5px;'>Final Score</h3>" +
                "<p style='font-size: 14px; margin: 0;'>" +
                "<span style='color: blue;'>Player 1: " + session.getScore(Player.Player1) + "</span>" +
                " &nbsp;|&nbsp; " +
                "<span style='color: red;'>Player 2: " + session.getScore(Player.Player2) + "</span>" +
                "</p>" +

                "<br></body></html>";

        JLabel label = new JLabel(html);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);

        JOptionPane.showMessageDialog(this, panel, "Game Over", JOptionPane.PLAIN_MESSAGE);

        System.exit(0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBoxes(g2d);
        drawLines(g2d);
        drawHover(g2d);
        drawDots(g2d);
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
        Player current = session.getCurrentPlayer();
        String p1Text = "Player 1: " + session.getScore(Player.Player1);
        String p2Text = "Player 2: " + session.getScore(Player.Player2);
        String separator = "  |  ";

        int startX = OFFSET;
        int y = OFFSET / 2;

        if (current == Player.Player1 && !session.isGameOver()) {
            g2d.setColor(Color.BLUE);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
        } else {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        }
        g2d.drawString(p1Text, startX, y);

        int p1Width = g2d.getFontMetrics().stringWidth(p1Text);
        startX += p1Width;

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString(separator, startX, y);

        int sepWidth = g2d.getFontMetrics().stringWidth(separator);
        startX += sepWidth;

        if (current == Player.Player2 && !session.isGameOver()) {
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
        } else {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        }
        g2d.drawString(p2Text, startX, y);
    }

    private Color getPlayerColor(Player player) {
        if (player == Player.Player1) return Color.BLUE;
        if (player == Player.Player2) return Color.RED;
        return Color.BLACK;
    }

    private void drawBoxes(Graphics2D g2d) {
        // Iterate through all potential boxes (width-1, height-1)
        for (int r = 0; r < session.getHeight() - 1; r++) {
            for (int c = 0; c < session.getWidth() - 1; c++) {

                Player owner = session.getBoxOwner(new Point(r, c));

                if (owner != null) {
                    // Set color with Transparency (Alpha = 50 out of 255)
                    Color baseColor = getPlayerColor(owner);
                    g2d.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 50));

                    // Calculate coordinates
                    int x = OFFSET + (c * SPACING);
                    int y = OFFSET + (r * SPACING);

                    // Fill the square
                    g2d.fillRect(x, y, SPACING, SPACING);
                }
            }
        }
    }
}