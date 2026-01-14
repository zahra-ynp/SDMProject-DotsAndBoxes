package it.units.sdm.dotsandboxes.model;

public record Line(Point p1, Point p2) {
    public Line {
        if (p1.equals(p2)) throw new IllegalArgumentException("Line must connect two different points");

        if (shouldSwap(p1, p2)) {
            Point temp = p1;
            p1 = p2;
            p2 = temp;
        }
    }

    private boolean shouldSwap(Point a, Point b) {
        // If row A is bigger than row B, we must swap (return true)
        if (a.row() != b.row()) {
            return a.row() > b.row();
        }
        // If rows are same, but col A is bigger than col B, we must swap (return true)
        return a.col() > b.col();
    }
}