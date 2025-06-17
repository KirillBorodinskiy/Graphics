package org.borodkir.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ConvexFill {
    /**
     * Sets a point on the canvas and adds it to the list of points.
     * The point is drawn as a small rectangle.
     * The size of the rectangle is 3x3 pixels, making it easier to see.
     *
     * @param points The list of points to which the new point will be added.
     * @param canvas The canvas on which the point will be drawn.
     * @param x      The x-coordinate of the point.
     * @param y      The y-coordinate of the point.
     */
    public static void setPoint(List<Point> points, Canvas canvas, int x, int y) {

        // Check if the point is within the canvas bounds
        if (x < 0 || x >= canvas.getWidth() || y < 0 || y >= canvas.getHeight()) {
            throw new IllegalArgumentException("Point is out of canvas bounds");
        }
        // Add the point to the list
        points.add(new Point(x, y));

        // Draw the point on the canvas
        canvas.getGraphicsContext2D().fillRect(x - 1, y - 1, 3, 3);
    }

    /**
     * Fills the area defined by the given points using Pineda's algorithm.
     * This method assumes that the points form a convex polygon.
     *
     * @param points The list of points set in counter-clockwise order that define the convex polygon.
     * @param canvas The canvas on which to fill the area.
     */
    public static void pinedaFill(ArrayList<Point> points, Canvas canvas) {
        if (points.size() < 3) {
            Main.showAlert("You must have at least 3 points to fill");
            return;
        }
        Main.showAlert("Filling from " + points.size() + " points using Pineda's algorithm");

        BoundingBox boundingBox = BoundingBox.createBoundingBox(points, canvas);
        Color fillColor = Color.color(Math.random(), Math.random(), Math.random());
        var pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();

        // Precompute edge functions
        List<EdgeFunction> edges = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % points.size());// Wrap around to the first point
            edges.add(new EdgeFunction(p1, p2));
        }

        // Scan through bounding box
        for (int y = boundingBox.startY; y <= boundingBox.endY; y++) {
            // Find the x range for this scanline
            int minX = boundingBox.startX;
            int maxX = boundingBox.endX;

            // Initialize edge values for the leftmost pixel on this scanline
            double[] edgeValues = new double[edges.size()];
            for (int i = 0; i < edges.size(); i++) {
                edgeValues[i] = edges.get(i).isInside(minX, y);
            }

            // Fill the scanline
            for (int x = minX; x <= maxX; x++) {
                boolean inside = true;

                // Check if inside all edges
                for (double value : edgeValues) {
                    if (value <= 0) {
                        inside = false;
                        break;
                    }
                }

                if (inside) {
                    pixelWriter.setColor(x, y, fillColor);
                }

                // Increment edge values using the edge slopes
                for (int i = 0; i < edges.size(); i++) {
                    edgeValues[i] += edges.get(i).a;
                }
            }
        }
        points.clear();
    }
}
