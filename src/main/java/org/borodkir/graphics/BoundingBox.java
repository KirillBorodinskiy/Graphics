package org.borodkir.graphics;

import javafx.scene.canvas.Canvas;

import java.util.ArrayList;

public class BoundingBox {
    final int startX;
    final int startY;
    final int endX;
    final int endY;

    public BoundingBox(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public static BoundingBox createBoundingBox(ArrayList<Point> points, Canvas canvas) {
        if (points.isEmpty()) {
            throw new IllegalArgumentException("Point list cannot be empty");
        }

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point point : points) {
            if (point.x < minX) {
                minX = point.x;
            }
            if (point.y < minY) {
                minY = point.y;
            }
            if (point.x > maxX) {
                maxX = point.x;
            }
            if (point.y > maxY) {
                maxY = point.y;
            }
        }

        // Ensure the bounding box is within the canvas bounds
        minX = Math.max(minX, 0);
        minY = Math.max(minY, 0);
        maxX = Math.min(maxX, (int) canvas.getWidth() - 1);
        maxY = Math.min(maxY, (int) canvas.getHeight() - 1);

        return new BoundingBox(minX, minY, maxX, maxY);
    }
}
