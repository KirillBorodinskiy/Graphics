package org.borodkir.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class FillAlg {

    /**
     * Fills the area starting from the given point (startX, startY) with a random color.
     * Delegates the filling process to a private floodFill method.
     *
     * @param useDFS      If true, uses Depth-First Search; otherwise, uses Breadth-First Search.
     * @param canvas      The canvas on which to perform the fill operation.
     * @param gc          The GraphicsContext of the canvas to draw on.
     * @param startX      The x-coordinate of the starting point for the fill.
     * @param startY      The y-coordinate of the starting point for the fill.
     * @param diagonals   If true, allows diagonal filling; otherwise, only fills orthogonally.
     */
    protected static void fillFromPoint(
            boolean useDFS,
            Canvas canvas,
            GraphicsContext gc,
            int startX,
            int startY,
            boolean diagonals
    ) {
        WritableImage snapshot = canvas.snapshot(null, null);
        int width = (int) snapshot.getWidth();
        int height = (int) snapshot.getHeight();

        Color targetColor = snapshot.getPixelReader().getColor(startX, startY);
        Color replacement = Color.rgb(
                (int) (Math.random() * 255),
                (int) (Math.random() * 255),
                (int) (Math.random() * 255));
        if (targetColor.equals(replacement)) return;

        boolean[][] visited = new boolean[width][height];
        floodFill(visited, width, height, startX, startY, targetColor, replacement, snapshot, gc, useDFS, diagonals);
    }

    /**
     * Performs a flood fill operation using either Depth-First Search (DFS) or Breadth-First Search (BFS).
     * @param visited A 2D array to keep track of visited pixels.
     * @param width The width of the canvas.
     * @param height The height of the canvas.
     * @param startX The x-coordinate of the starting point for the fill.
     * @param startY The y-coordinate of the starting point for the fill.
     * @param targetColor The color of the target area to fill.
     * @param replacement The color to replace the target color with.
     * @param snapshot The snapshot of the canvas.
     * @param gc The GraphicsContext of the canvas to draw on.
     * @param useDFS If true, uses Depth-First Search; otherwise, uses Breadth-First Search.
     * @param diagonals If true, allows diagonal filling; otherwise, only fills orthogonally.
     */
    private static void floodFill(
            boolean[][] visited,
            int width,
            int height,
            int startX,
            int startY,
            Color targetColor,
            Color replacement,
            WritableImage snapshot,
            GraphicsContext gc,
            boolean useDFS,
            boolean diagonals
    ) {
        FloodContainer container;
        if (useDFS) {
            container = new StackContainer();
        } else {
            container = new QueueContainer();
        }
        container.add(startX, startY);
        final int BATCH_SIZE = 500;
        javafx.animation.AnimationTimer timer = new javafx.animation.AnimationTimer() {
            int processedInBatch = 0;

            @Override
            public void handle(long now) {
                processedInBatch = 0;

                while (!container.isEmpty() && processedInBatch < BATCH_SIZE) {
                    Pair<Integer, Integer> pos = container.get();
                    int x = pos.getKey();
                    int y = pos.getValue();

                    if (x < 0 || y < 0 || x >= width || y >= height || visited[x][y]) {
                        continue;
                    }

                    Color currentColor = snapshot.getPixelReader().getColor(x, y);
                    if (!currentColor.equals(targetColor)) {
                        continue;
                    }

                    // Mark as visited and fill
                    visited[x][y] = true;
                    gc.getPixelWriter().setColor(x, y, replacement);

                    // Add neighbors to the queue
                    container.add(x + 1, y);
                    container.add(x - 1, y);
                    container.add(x, y + 1);
                    container.add(x, y - 1);

                    //Diagonals
                    if (diagonals) {
                        container.add(x + 1, y + 1);
                        container.add(x - 1, y - 1);
                        container.add(x + 1, y - 1);
                        container.add(x - 1, y + 1);
                    }

                    processedInBatch++;
                }

                if (container.isEmpty()) {
                    this.stop();
                }
            }
        };
        timer.start();
    }
}
