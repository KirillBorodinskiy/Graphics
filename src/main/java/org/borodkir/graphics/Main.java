package org.borodkir.graphics;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;

public class Main extends Application {

    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Algorithm Visualizer");

        // input for the size of the canvas
        double width = 800;
        double height = 600;

        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Set the background color of the canvas
        canvas.setStyle("-fx-background-color: white;");

        // Add a simple drawing to the canvas
        setupCanvas(gc, canvas, width, height);
        canvas.setOnMouseClicked(event -> fillFromPoint(gc, canvas, (int) event.getX(), (int) event.getY()));

        Group root = new Group(canvas);
        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void fillFromPoint(GraphicsContext gc, Canvas canvas, int startX, int startY) {
        WritableImage snapshot = canvas.snapshot(null, null);
        PixelReader reader = snapshot.getPixelReader();
        int width = (int) snapshot.getWidth();
        int height = (int) snapshot.getHeight();

        Color targetColor = reader.getColor(startX, startY);
        Color replacement = Color.rgb(
                (int) (Math.random() * 255),
                (int) (Math.random() * 255),
                (int) (Math.random() * 255));
        if (targetColor.equals(replacement))
            return;
        ArrayList<Pair<Integer, Integer>> visited = new ArrayList<>();
        floodFillDFS(gc, visited, width, height, startX, startY, targetColor, replacement,snapshot);
    }

    private void floodFillDFS(GraphicsContext gc, ArrayList<Pair<Integer, Integer>> visited, int width,
                              int height, int x, int y, Color clickedColor, Color newColor,WritableImage snapshot) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return; // Skip out-of-bounds coordinates
        }
        if (visited.contains(new Pair<>(x, y))) {
            return; // Skip already visited pixels
        }

        Color currentColor = snapshot.getPixelReader().getColor(x, y);

        if (currentColor.equals(newColor) || !currentColor.equals(clickedColor)) {
            return; // Skip if the pixel is already filled with the new color
        }


        snapshot.getPixelWriter().setColor(x, y, newColor);
        gc.setFill(newColor);
        gc.fillRect(x, y, 1, 1); // Fill the pixel with the new color
        visited.add(new Pair<>(x, y));

        floodFillDFS(gc, visited, width, height, x, y + 1, clickedColor, newColor,snapshot);
        floodFillDFS(gc, visited, width, height, x, y - 1, clickedColor, newColor,snapshot);

        floodFillDFS(gc, visited, width, height, x + 1, y, clickedColor, newColor,snapshot);
        floodFillDFS(gc, visited, width, height, x - 1, y, clickedColor, newColor,snapshot);

        floodFillDFS(gc, visited, width, height, x + 1, y + 1, clickedColor, newColor,snapshot);
        floodFillDFS(gc, visited, width, height, x - 1, y - 1, clickedColor, newColor,snapshot);
    }

    private void setupCanvas(GraphicsContext gc, Canvas canvas, double width, double height) {
        this.canvas = canvas;
        // Face
        gc.setStroke(javafx.scene.paint.Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeOval(width / 8, height / 8, width / 1.5, height / 1.5);

        // EYE 1
        gc.setStroke(Color.GREEN);
        gc.setLineWidth(2);
        gc.strokeOval(width / 4, height / 4, width / 8, height / 8);

        // EYE 2
        gc.setFill(Color.YELLOW);
        gc.setLineWidth(2);
        gc.strokeOval(width / 2, height / 4, width / 8, height / 8);

        // MOUTH
        gc.setFill(Color.RED);
        gc.setLineWidth(2);
        gc.strokeArc(width / 3, height / 2, width / 4, height / 4, 0, -180, javafx.scene.shape.ArcType.OPEN);

        // Frame
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10);
        gc.setStroke(Color.RED);
        gc.strokeRect(10, 30, width - 20, height - 40);

    }

    public static void main(String[] args) {
        launch(args);
    }
}