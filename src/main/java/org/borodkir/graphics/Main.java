package org.borodkir.graphics;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {
    private final ArrayList<Point> points = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Fill Algorithm Visualizer");
        // Add to your main method before launch()
        System.setProperty("prism.antialiastext", "false");
        System.setProperty("prism.vsync", "false");
        System.setProperty("prism.subpixeltext", "false");

        // Canvas setup
        double width = 800;
        double height = 600;
        Canvas canvas = new Canvas(width, height);
        canvas.setStyle("-fx-background-color: white;");
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // Disable antialiasing for lines and shapes
        gc.setLineWidth(1.0); // Use exact pixel widths
        gc.setLineCap(StrokeLineCap.SQUARE); // Avoid round caps
        gc.setLineJoin(StrokeLineJoin.MITER); // Sharp corners
        gc.setImageSmoothing(false);

        canvas.setCache(true);
        canvas.setCacheHint(CacheHint.SPEED);

        setupCanvas(gc, width, height);


        // Algorithm selection
        ToggleGroup algorithmGroup = new ToggleGroup();
        RadioButton dfsRadioButton = new RadioButton("DFS");
        RadioButton bfsRadioButton = new RadioButton("BFS");
        dfsRadioButton.setToggleGroup(algorithmGroup);
        bfsRadioButton.setToggleGroup(algorithmGroup);
        dfsRadioButton.setSelected(true);

        // Diagonal selection
        ToggleGroup diagonalsGroup = new ToggleGroup();
        RadioButton diagonalButton = new RadioButton("With Diagonals");
        RadioButton nonDiagonalButton = new RadioButton("Without Diagonals");
        diagonalButton.setToggleGroup(diagonalsGroup);
        nonDiagonalButton.setToggleGroup(diagonalsGroup);
        nonDiagonalButton.setSelected(true);

        // Mode selection
        ToggleGroup modeGroup = new ToggleGroup();
        RadioButton fillAllModeButton = new RadioButton("Flood Fill");
        RadioButton drawModeButton = new RadioButton("Convex Fill");
        Button fillConvexShapeButton = new Button("Fill Shape");
        fillAllModeButton.setToggleGroup(modeGroup);
        drawModeButton.setToggleGroup(modeGroup);
        fillAllModeButton.setSelected(true);

        // Layout
        VBox controlsBox = new VBox(10);
        controlsBox.setPadding(new Insets(10));
        controlsBox.setStyle("-fx-background-color: #f8f8f8;");

        // Algorithm controls
        TitledPane algorithmPane = new TitledPane("Fill Algorithm", new VBox(5,
                new Label("Select Algorithm:"),
                new HBox(10, dfsRadioButton, bfsRadioButton)
        ));
        algorithmPane.setExpanded(true);

        // Diagonal controls
        TitledPane diagonalPane = new TitledPane("Diagonal Options", new VBox(5,
                new Label("Fill Direction:"),
                new HBox(10, diagonalButton, nonDiagonalButton)
        ));
        diagonalPane.setExpanded(true);

        // Mode controls
        TitledPane modePane = new TitledPane("Fill Mode", new VBox(5,
                new Label("Select Mode:"),
                new HBox(10, fillAllModeButton, drawModeButton),
                fillConvexShapeButton
        ));
        modePane.setExpanded(true);

        // Color picker
        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setPrefWidth(100);
        TitledPane colorPane = getColorPane(colorPicker);

        controlsBox.getChildren().addAll(algorithmPane, diagonalPane, modePane, colorPane);

        // Main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setLeft(controlsBox);
        mainLayout.setCenter(canvas);

        // Event handlers
        fillConvexShapeButton.setOnAction(event -> {
            if (drawModeButton.isSelected()) {
                ConvexFill.pinedaFill(points, canvas);
            }
        });

        canvas.setOnMouseClicked(event -> {
            Color selectedColor = colorPicker.getValue();
            if (fillAllModeButton.isSelected()) {
                boolean useDFS = dfsRadioButton.isSelected();
                boolean diagonals = diagonalButton.isSelected();
                FillAlg.fillFromPoint(useDFS, canvas, gc, (int) event.getX(), (int) event.getY(), diagonals, selectedColor);
            } else {
                ConvexFill.setPoint(points, canvas, (int) event.getX(), (int) event.getY(), selectedColor);
            }
        });

        Scene scene = new Scene(mainLayout, width + 300, height + 50);
        Platform.setImplicitExit(true);
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static TitledPane getColorPane(ColorPicker colorPicker) {
        TitledPane colorPane = new TitledPane("Color", new VBox(5,
                new Label("Select Fill Color:"),
                new HBox(10, colorPicker, new Button("Random") {{
                    setOnAction(e -> {
                        Color randomColor = Color.rgb(
                                (int) (Math.random() * 256),
                                (int) (Math.random() * 256),
                                (int) (Math.random() * 256)
                        );
                        colorPicker.setValue(randomColor);
                    });
                }})
        ));
        colorPane.setExpanded(true);
        return colorPane;
    }


    private void setupCanvas(GraphicsContext gc, double width, double height) {
        gc.setImageSmoothing(false);
        // FACE
        gc.setStroke(Color.BLUE);
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
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);
        gc.strokeArc(width / 3, height / 2, width / 4, height / 4, 0, -180, ArcType.OPEN);

        // FRAME
        gc.setStroke(Color.RED);
        gc.setLineWidth(10);
        gc.strokeRect(10, 30, width - 20, height - 40);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}