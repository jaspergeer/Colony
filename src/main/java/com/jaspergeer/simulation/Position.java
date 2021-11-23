package com.jaspergeer.simulation;

public class Position {
    private int x;
    private int y;
    private final int xDim;
    private final int yDim;

    public Position(int y, int x, int yDim, int xDim) {
        this.xDim = xDim;
        this.yDim = yDim;
        this.x = Utils.clamp(x, 0, xDim - 1);
        this.y = Utils.clamp(y, 0, yDim - 1);
    }

    /**
     * copy constructor
     */
    public Position(Position p) {
        this.xDim = p.xDim;
        this.yDim = p.yDim;
        this.x = p.x;
        this.y = p.y;
    }

    /**
     * copy constructor with adjusted max values
     */
    public Position(Position p, int yDim, int xDim) {
        this.xDim = xDim;
        this.yDim = yDim;
        this.x = p.x;
        this.y = p.y;
    }

    /**
     * Get position resulting from a given change in x and y
     * @param shiftY amount to shift y by
     * @param shiftX amount to shift x by
     * @return resulting position
     */
    public Position getResultOf(int shiftY, int shiftX) {
        Position result = new Position(this);
        result.adjust(shiftY, shiftX);
        return result;
    }

    /**
     * Adjust x and y by given amounts. Neither value can go above the associated Dim - 1 or
     * below 0
     * @param shiftY amount to shift y by
     * @param shiftX amount to shift x by
     */
    public void adjust(int shiftY, int shiftX) {
        x = Utils.clamp(x + shiftX, 0, xDim - 1);
        y = Utils.clamp(y + shiftY, 0, yDim - 1);
    }

    public boolean equals(Position other) {
        return other.x == x && other.y == y && other.xDim == xDim && other.yDim == yDim;
    }

    public void setX(int newX) { x = Utils.clamp(newX, 0, xDim - 1); }

    public void setY(int newY) { y = Utils.clamp(newY, 0, yDim - 1); }

    public int getX() { return x; }

    public int getY() { return y; }

}
