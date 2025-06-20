package org.borodkir.graphics;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

import java.util.ArrayList;

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


        // Radio buttons for algorithm selection
        ToggleGroup algorithmGroup = new ToggleGroup();

        RadioButton dfsRadioButton = new RadioButton("DFS (Depth-First Search)");
        RadioButton bfsRadioButton = new RadioButton("BFS (Breadth-First Search)");

        dfsRadioButton.setToggleGroup(algorithmGroup);
        bfsRadioButton.setToggleGroup(algorithmGroup);

        dfsRadioButton.setSelected(true); // Default to DFS
        HBox algBox = new HBox(10, dfsRadioButton, bfsRadioButton);


        // Radio buttons for diagonal filling
        ToggleGroup diagonalsGroup = new ToggleGroup();

        RadioButton diagonalButton = new RadioButton("With Diagonals");
        RadioButton nonDiagonalButton = new RadioButton("Without Diagonals");

        diagonalButton.setToggleGroup(diagonalsGroup);
        nonDiagonalButton.setToggleGroup(diagonalsGroup);

        nonDiagonalButton.setSelected(true); // Default to non-diagonal
        HBox diagonalBox = new HBox(10, diagonalButton, nonDiagonalButton);


        //Points for convex fill
        ArrayList<Point> points = new ArrayList<>();
        // Radio buttons for fill mode selection (Pineda or Flood Fill)
        ToggleGroup modeGroup = new ToggleGroup();
        RadioButton fillAllModeButton = new RadioButton("Fill space");
        RadioButton drawModeButton = new RadioButton("Set points for convex fill");

        Button FillConvexShapeButton = new Button("Fill Shape");// Button to fill the convex shape
        FillConvexShapeButton.setOnAction(event -> {
            if (drawModeButton.isSelected()) {
                ConvexFill.pinedaFill(points, canvas);
            }
        });

        fillAllModeButton.setToggleGroup(modeGroup);
        drawModeButton.setToggleGroup(modeGroup);
        fillAllModeButton.setSelected(true); // Default to fill mode
        HBox modeBox = new HBox(10, fillAllModeButton, drawModeButton, FillConvexShapeButton);


        // Set up the canvas mouse click event
        canvas.setOnMouseClicked(event -> {
            if (fillAllModeButton.isSelected()) {// Flood fill mode
                boolean useDFS = dfsRadioButton.isSelected();
                boolean diagonals = diagonalButton.isSelected();
                FillAlg.fillFromPoint(useDFS, canvas, gc, (int) event.getX(), (int) event.getY(), diagonals);
            } else {// Pineda's fill mode
                ConvexFill.setPoint(points, canvas, (int) event.getX(), (int) event.getY());
            }
        });


        VBox root = new VBox(10, algBox, diagonalBox, modeBox, canvas);
        Scene scene = new Scene(root, width, height*1.15);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Sets up the canvas with a face drawing and a frame.
     */
    private void setupCanvas(GraphicsContext gc, double width, double height) {
        // FACE
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
        gc.strokeArc(width / 3, height / 2, width / 4, height / 4, 0, -180, ArcType.OPEN);

        // FRAME
        gc.setLineWidth(10);
        gc.setStroke(Color.RED);
        gc.strokeRect(10, 30, width - 20, height - 40);

    }

    public static void main(String[] args) {
        launch(args);
    }

    // Show a simple information alert
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}