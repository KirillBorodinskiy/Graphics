package org.borodkir.graphics;

import javafx.scene.paint.Color;

public class Point {
    final int x;
    final int y;
    final Color color;

    public Point(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color; // Default color
    }
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.color = Color.BLACK; // Default color
    }
}
