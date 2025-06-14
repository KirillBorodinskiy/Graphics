package org.borodkir.graphics;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Algorithm Visualizer");

        // input for the size of the canvas
        double width = 800;
        double height = 600;

        Canvas canvas = new Canvas(width, height);
        canvas.setStyle("-fx-background-color: white;");

        GraphicsContext gc = canvas.getGraphicsContext2D();

        setupCanvas(gc, width, height);

        ToggleGroup algorithmGroup = new ToggleGroup();

        RadioButton dfsRadioButton = new RadioButton("DFS (Depth-First Search)");
        RadioButton bfsRadioButton = new RadioButton("BFS (Breadth-First Search)");

        dfsRadioButton.setToggleGroup(algorithmGroup);
        bfsRadioButton.setToggleGroup(algorithmGroup);

        dfsRadioButton.setSelected(true); // Default to DFS

        HBox controlBox = new HBox(10, dfsRadioButton, bfsRadioButton);
        controlBox.setPadding(new Insets(10));


        // Add the radio buttons to the canvas
        canvas.setOnMouseClicked(event -> {
            boolean useDFS = dfsRadioButton.isSelected();
            FillAlg.fillFromPoint(useDFS,gc, canvas, (int) event.getX(), (int) event.getY());
        });


        // Set the background color of the canvas

        VBox root = new VBox(10, controlBox, canvas);
        Scene scene = new Scene(root, width, height + 50);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupCanvas(GraphicsContext gc, double width, double height) {
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