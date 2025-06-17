package org.borodkir.graphics;

public class EdgeFunction {
    public final int a;
    public final int b;
    public final int c;

    public EdgeFunction(Point p1, Point p2) {
        this.a = p2.y - p1.y;
        this.b = p1.x - p2.x;
        this.c = p2.x * p1.y - p1.x * p2.y;
    }

    /**
     * Returns a positive value if the point (x, y) is inside the edge defined by p1 and p2,
     * a negative value if it is outside, and zero if it is on the edge.
     */
    public double isInside(int x, int y) {
        return a * x + b * y + c;
    }
}
