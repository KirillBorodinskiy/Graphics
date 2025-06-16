package org.borodkir.graphics;

public class EdgeFunction {
    public final double a;
    public final double b;
    public final double c;

    public EdgeFunction(Point p1, Point p2) {
        // Edge function: E(x,y) = (y2-y1)x + (x1-x2)y + (x2y1 - x1y2)
        this.a = p2.y - p1.y;
        this.b = p1.x - p2.x;
        this.c = p2.x * p1.y - p1.x * p2.y;
    }

    public double evaluate(int x, int y) {
        return a * x + b * y + c;
    }
}
