package org.borodkir.graphics;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ConvexFill {
    public static void setPoint(List<Point> points, Canvas canvas, int x, int y, Color color) {
        // Check if the point is within the canvas bounds
        if (x-1 < 0 || x+1 >= canvas.getWidth() || y-1 < 0 || y+1 >= canvas.getHeight()) {
            throw new IllegalArgumentException("Point is out of canvas bounds");
        }
        // add the point to the list
        points.add(new Point(x, y, color));

        //Draw the point on the canvas
        var gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        gc.setFill(color);
        gc.fillRect(x - 1, y - 1, 3, 3);
    }

    public static void pinedaFill(ArrayList<Point> points, Canvas canvas) {
        if (points.size() < 3) {
            Platform.runLater(() -> {
                Main.alert("At least 3 points is required for filling.");
            });
            return;
        }
        Platform.runLater(() -> {
            Main.alert("filling with " + points.size() + " points.");
            points.clear();
        });
        
        BoundingBox boundingBox = BoundingBox.createBoundingBox(points, canvas);
        Color fillColor = points.getFirst().color;
        var gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        var pixelWriter = gc.getPixelWriter();

        // Precompute edge functions
        List<EdgeFunction> edges = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % points.size());// Wrap around to the first point
            edges.add(new EdgeFunction(p1, p2));
        }

        // Scan through bounding box
        for (int y = boundingBox.startY; y <= boundingBox.endY; y++) {
            // Initialize edge values for the leftmost pixel on this scanline
            double[] edgeValues = new double[edges.size()];
            for (int i = 0; i < edges.size(); i++) {
                edgeValues[i] = edges.get(i).evaluate(boundingBox.startX, y);
            }

            // Fill the scanline
            for (int x = boundingBox.startX; x <= boundingBox.endX; x++) {
                boolean inside = true;

                // Check if inside all edges or on an edge
                for (double value : edgeValues) {
                    if (value < 0) {
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
    }
}
