package it.units.sdm.dotsandboxes.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LineTest {

    @Test
    void equalsIndependentOfPointOrder() {
        Point a = new Point(0, 0);
        Point b = new Point(0, 1);

        Line l1 = new Line(a, b);
        Line l2 = new Line(b, a);

        assertEquals(l1, l2);
        assertEquals(l1.hashCode(), l2.hashCode());
    }

    @Test
    void rejectsSelfLoop() {
        Point p = new Point(1, 1);
        assertThrows(IllegalArgumentException.class, () -> new Line(p, p));
    }

    @Test
    void rejectsDiagonalLine() {
        assertThrows(IllegalArgumentException.class,
                () -> new Line(new Point(0, 0), new Point(1, 1)));
    }

    @Test
    void rejectsNonAdjacentLine() {
        assertThrows(IllegalArgumentException.class,
                () -> new Line(new Point(0, 0), new Point(0, 2)));
    }
}

