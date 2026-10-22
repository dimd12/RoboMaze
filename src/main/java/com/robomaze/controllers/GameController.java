package com.robomaze.controllers;

import static com.robomaze.models.TileType.EMPTY;
import static com.robomaze.models.TileType.ROBOT;
import static com.robomaze.models.TileType.TARGET;
import static com.robomaze.models.TileType.WALL;

import com.robomaze.algorithms.FastestPathAlg;
import com.robomaze.algorithms.MazeGeneration;
import com.robomaze.models.Robot;
import com.robomaze.models.TileType;
import java.awt.Point;
import java.util.List;
import java.util.Objects;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class GameController {

    @FXML
    private GridPane grid;

    private TileType[][] tiles;
    private TileType[][] originalTiles;

    private Robot robot;

    @FXML
    private Button btnForward;

    @FXML
    private Button btnTurnLeft;

    @FXML
    private Button btnTurnRight;

    @FXML
    private Label labelMoves;

    @FXML
    private Label labelShortest;

    @FXML
    private ListView<String> listMoves;

    @FXML
    private Button btnRun;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnNewMaze;

    @FXML
    private Button btnViewPath;

    private Timeline timeline;

    @FXML
    private void initialize() {
        initializeMaze();
        initializeRobot();
        setActions();
        enableButtons();
        renderMaze(List.of());
    }

    private void initializeMaze() {
        tiles = MazeGeneration.generateMaze(15, 15);
        saveOriginalMaze();
    }

    private void saveOriginalMaze() {
        originalTiles = new TileType[tiles.length][];

        for (int i = 0; i < tiles.length; i++) {
            originalTiles[i] = tiles[i].clone();
        }
    }

    private void initializeRobot() {
        Point position = getRobot(tiles);

        robot = new Robot(position.x, position.y);
        robot.setDirection("RIGHT");
    }

    private void setActions() {
        btnForward.setOnAction(e -> moves("FORWARD"));
        btnTurnLeft.setOnAction(e -> moves("TURN_LEFT"));
        btnTurnRight.setOnAction(e -> moves("TURN_RIGHT"));
        btnRun.setOnAction(e -> runMoves(listMoves.getItems()));
        btnReset.setOnAction(e -> reset());
        btnViewPath.setOnAction(e -> viewFastestPath());
        btnNewMaze.setOnAction(e -> generateMaze());
    }

    private void enableButtons() {
        btnRun.setDisable(false);
        btnReset.setDisable(false);
        btnNewMaze.setDisable(false);
        btnForward.setDisable(false);
        btnTurnLeft.setDisable(false);
        btnTurnRight.setDisable(false);
        btnViewPath.setDisable(false);
    }

    private void renderMaze(List<Point> highlightedPath) {
        grid.getChildren().clear();
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();

        int rows = tiles.length;
        int cols = tiles[0].length;

        for (int col = 0; col < cols; col++) {
            ColumnConstraints constraint = new ColumnConstraints();
            constraint.setPercentWidth(100.0 / cols);
            constraint.setHgrow(Priority.ALWAYS);
            constraint.setFillWidth(true);
            grid.getColumnConstraints().add(constraint);
        }

        for (int row = 0; row < rows; row++) {
            RowConstraints constraint = new RowConstraints();
            constraint.setPercentHeight(100.0 / rows);
            constraint.setVgrow(Priority.ALWAYS);
            constraint.setFillHeight(true);
            grid.getRowConstraints().add(constraint);
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                TileType type = tiles[row][col];
                StackPane cell = new StackPane();

                boolean isPath = highlightedPath.contains(
                        new Point(row, col)
                );

                if (type == WALL) {
                    cell.getChildren().add(createSprite("wall.png"));
                } else {
                    String floorSprite = isPath
                            ? "floor_red.png"
                            : "floor.png";

                    cell.getChildren().add(createSprite(floorSprite));

                    if (type == ROBOT) {
                        String robotSprite = switch (robot.getDirection()) {
                            case "UP" ->
                                "robot2.png";
                            case "DOWN" ->
                                "robot0.png";
                            case "LEFT" ->
                                "robot3.png";
                            case "RIGHT" ->
                                "robot1.png";
                            default ->
                                "robot1.png";
                        };

                        cell.getChildren().add(
                                createSprite(robotSprite)
                        );
                    } else if (type == TARGET) {
                        cell.getChildren().add(
                                createSprite("target.png")
                        );
                    }
                }

                grid.add(cell, col, row);
            }
        }
    }

    private ImageView createSprite(String name) {
        String path = "/com/robomaze/images/sprites/" + name;

        var stream = Objects.requireNonNull(
                getClass().getResourceAsStream(path),
                "Sprite not found: " + path
        );

        Image image = new Image(stream);

        ImageView imageView = new ImageView(image);

        imageView.fitWidthProperty().bind(
                grid.widthProperty()
                        .subtract(30)
                        .divide(tiles[0].length)
        );

        imageView.fitHeightProperty().bind(
                grid.heightProperty()
                        .subtract(30)
                        .divide(tiles.length)
        );

        imageView.setPreserveRatio(false);
        imageView.setSmooth(false);

        return imageView;
    }

    private void reset() {
        if (timeline != null) {
            timeline.stop();
        }

        tiles = new TileType[originalTiles.length][];

        for (int i = 0; i < originalTiles.length; i++) {
            tiles[i] = originalTiles[i].clone();
        }

        initializeRobot();

        listMoves.getItems().clear();
        labelMoves.setText("Moves: 0");
        labelShortest.setText("Shortest: -");

        enableButtons();
        renderMaze(List.of());
    }

    private void runMoves(List<String> moves) {
        if (timeline != null) {
            timeline.stop();
        }

        List<String> commands = List.copyOf(moves);

        if (commands.isEmpty()) {
            return;
        }

        final int[] index = {0};

        timeline = new Timeline(
                new KeyFrame(Duration.millis(250), event -> {
                    String move = commands.get(index[0]++);

                    switch (move) {
                        case "FORWARD" ->
                            forward();
                        case "TURN_LEFT" ->
                            turnLeft();
                        case "TURN_RIGHT" ->
                            turnRight();
                        default -> {
                        }
                    }
                })
        );

        timeline.setCycleCount(commands.size());
        timeline.play();
    }

    @FXML
    private void moves(String input) {
        switch (input) {
            case "FORWARD", "TURN_LEFT", "TURN_RIGHT" ->
                listMoves.getItems().add(input);
            default -> {
            }
        }

        labelMoves.setText(
                "Moves: " + listMoves.getItems().size()
        );
    }

    private void forward() {
        int oldRow = robot.getRow();
        int oldCol = robot.getCol();

        int newRow = oldRow;
        int newCol = oldCol;

        switch (robot.getDirection()) {
            case "UP" ->
                newRow--;
            case "DOWN" ->
                newRow++;
            case "LEFT" ->
                newCol--;
            case "RIGHT" ->
                newCol++;
            default -> {
            }
        }

        if (newRow >= 0 && newRow < tiles.length
                && newCol >= 0 && newCol < tiles[0].length
                && tiles[newRow][newCol] != WALL) {

            tiles[oldRow][oldCol]
                    = originalTiles[oldRow][oldCol] == TARGET
                            ? TARGET
                            : EMPTY;

            robot.setRow(newRow);
            robot.setCol(newCol);

            tiles[newRow][newCol] = ROBOT;
        }

        renderMaze(List.of());
    }

    private void turnLeft() {
        switch (robot.getDirection()) {
            case "UP" ->
                robot.setDirection("LEFT");
            case "LEFT" ->
                robot.setDirection("DOWN");
            case "DOWN" ->
                robot.setDirection("RIGHT");
            case "RIGHT" ->
                robot.setDirection("UP");
            default -> {
            }
        }

        renderMaze(List.of());
    }

    private void turnRight() {
        switch (robot.getDirection()) {
            case "UP" ->
                robot.setDirection("RIGHT");
            case "RIGHT" ->
                robot.setDirection("DOWN");
            case "DOWN" ->
                robot.setDirection("LEFT");
            case "LEFT" ->
                robot.setDirection("UP");
            default -> {
            }
        }

        renderMaze(List.of());
    }

    private void generateMaze() {
        Dialog<int[]> dialog = new Dialog<>();

        dialog.setTitle("Maze Settings");
        dialog.setHeaderText(
                "Enter the maze dimensions.\n"
                + "Rows and columns must be odd numbers."
        );
        dialog.setResizable(false);

        TextField rowsField = new TextField("15");
        TextField colsField = new TextField("15");

        GridPane dialogGrid = new GridPane();
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);

        dialogGrid.add(new Label("Rows:"), 0, 0);
        dialogGrid.add(rowsField, 1, 0);
        dialogGrid.add(new Label("Columns:"), 0, 1);
        dialogGrid.add(colsField, 1, 1);

        dialog.getDialogPane().setContent(dialogGrid);
        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK,
                ButtonType.CANCEL
        );

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    int rows = Integer.parseInt(rowsField.getText());
                    int cols = Integer.parseInt(colsField.getText());

                    if (rows < 5 || cols < 5
                            || rows % 2 == 0 || cols % 2 == 0) {
                        return null;
                    }

                    return new int[]{rows, cols};
                } catch (NumberFormatException e) {
                    return null;
                }
            }

            return null;
        });

        dialog.showAndWait().ifPresent(dimensions -> {
            if (timeline != null) {
                timeline.stop();
            }

            int rows = dimensions[0];
            int cols = dimensions[1];

            tiles = MazeGeneration.generateMaze(rows, cols);
            saveOriginalMaze();
            initializeRobot();

            listMoves.getItems().clear();
            labelMoves.setText("Moves: 0");
            labelShortest.setText("Shortest: -");

            enableButtons();
            renderMaze(List.of());
        });
    }

    private void viewFastestPath() {
        Point start = getRobot(originalTiles);
        Point target = getTarget(originalTiles);

        List<Point> path = FastestPathAlg.shortestPath(
                originalTiles,
                start,
                target
        );

        if (path.isEmpty()) {
            labelShortest.setText("Shortest: no path");
            return;
        }

        labelShortest.setText(
                "Shortest: "
                + FastestPathAlg.calculateMoves(path)
        );

        renderMaze(path);

        btnRun.setDisable(true);
        btnForward.setDisable(true);
        btnTurnLeft.setDisable(true);
        btnTurnRight.setDisable(true);
        btnViewPath.setDisable(true);
    }

    private Point getTarget(TileType[][] maze) {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                if (maze[row][col] == TARGET) {
                    return new Point(row, col);
                }
            }
        }

        throw new IllegalStateException("Target not found");
    }

    private Point getRobot(TileType[][] maze) {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                if (maze[row][col] == ROBOT) {
                    return new Point(row, col);
                }
            }
        }

        throw new IllegalStateException("Robot not found");
    }
}
