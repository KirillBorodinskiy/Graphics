package org.borodkir.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.Queue;

public class FillAlg {

    protected static void fillFromPoint(boolean useDFS, GraphicsContext gc, Canvas canvas, int startX, int startY) {
        WritableImage snapshot = canvas.snapshot(null, null);
        PixelReader reader = snapshot.getPixelReader();
        int width = (int) snapshot.getWidth();
        int height = (int) snapshot.getHeight();

        Color targetColor = reader.getColor(startX, startY);
        Color replacement = Color.rgb(
                (int) (Math.random() * 255),
                (int) (Math.random() * 255),
                (int) (Math.random() * 255));
        if (targetColor.equals(replacement)) return;

        boolean[][] visited = new boolean[width][height];
        if (useDFS) {
            floodFillDFS(gc, visited, width, height, startX, startY, targetColor, replacement, snapshot);
        } else{
            floodFillBFS(gc, visited, width, height, startX, startY, targetColor, replacement, snapshot);

        }
    }

    private static void floodFillBFS(GraphicsContext gc, boolean[][] visited, int width, int height, int startX, int startY, Color targetColor, Color replacement, WritableImage snapshot) {
        Queue <Pair<Integer, Integer>> queue = new java.util.LinkedList<>();
        queue.add(new Pair<>(startX, startY));
        final int BATCH_SIZE = 500;

        //TODO
    }

    protected static void floodFillDFS(GraphicsContext gc, boolean[][] visited, int width,
                                       int height, int x, int y, Color clickedColor, Color newColor, WritableImage snapshot) {
        // Use a stack to track positions we need to process
        java.util.Stack<Pair<Integer, Integer>> stack = new java.util.Stack<>();
        stack.push(new Pair<>(x, y));

        // Process a limited number of pixels per batch
        final int BATCH_SIZE = 500;

        javafx.animation.AnimationTimer timer = new javafx.animation.AnimationTimer() {
            int processedInBatch = 0;

            @Override
            public void handle(long now) {
                processedInBatch = 0;

                while (!stack.isEmpty() && processedInBatch < BATCH_SIZE) {
                    Pair<Integer, Integer> pos = stack.pop();
                    int px = pos.getKey();
                    int py = pos.getValue();

                    if (px < 0 || py < 0 || px >= width || py >= height || visited[px][py]) {
                        continue;
                    }

                    Color currentColor = snapshot.getPixelReader().getColor(px, py);
                    if (!currentColor.equals(clickedColor) || currentColor.equals(newColor)) {
                        continue;
                    }

                    // Mark as visited and fill
                    visited[px][py] = true;
                    snapshot.getPixelWriter().setColor(px, py, newColor);
                    gc.setFill(newColor);
                    gc.fillRect(px, py, 1, 1);

                    // Add neighbors to the stack
                    stack.push(new Pair<>(px, py + 1));
                    stack.push(new Pair<>(px, py - 1));
                    stack.push(new Pair<>(px + 1, py));
                    stack.push(new Pair<>(px - 1, py));

                    stack.push(new Pair<>(px + 1, py + 1));
                    stack.push(new Pair<>(px - 1, py - 1));
                    stack.push(new Pair<>(px + 1, py - 1));
                    stack.push(new Pair<>(px - 1, py + 1));

                    processedInBatch++;
                }

                if (stack.isEmpty()) {
                    this.stop();
                }
            }
        };

        timer.start();
    }

}
