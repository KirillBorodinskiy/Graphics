package org.borodkir.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.Queue;

public class FillAlg {

    protected static void fillFromPoint(
            boolean useDFS, GraphicsContext gc,
            Canvas canvas,
            int startX,
            int startY,
            boolean diagonals
    ) {
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
        floodFill(gc, visited, width, height, startX, startY, targetColor, replacement, snapshot, useDFS, diagonals);
    }

    private static void floodFill(
            GraphicsContext gc,
            boolean[][] visited,
            int width,
            int height,
            int startX,
            int startY,
            Color targetColor,
            Color replacement,
            WritableImage snapshot,
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
                    if (!currentColor.equals(targetColor) || currentColor.equals(replacement)) {
                        continue;
                    }

                    // Mark as visited and fill
                    visited[x][y] = true;
                    snapshot.getPixelWriter().setColor(x, y, replacement);
                    gc.setFill(replacement);
                    gc.fillRect(x, y, 1, 1);

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
