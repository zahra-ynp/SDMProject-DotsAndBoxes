package it.units.sdm.dotsandboxes.model;

public record Move(int row, int col, Direction direction) {

    public Line toLine() {
        Point p1 = new Point(this.row, this.col);
        Point p2;

        if (this.direction == Direction.HORIZONTAL) {
            p2 = new Point(this.row, this.col + 1);
        } else {
            p2 = new Point(this.row + 1, this.col);
        }

        return new Line(p1, p2);
    }
}
