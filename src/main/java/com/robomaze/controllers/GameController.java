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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

public class GameController {

    @FXML
    GridPane grid;

    TileType[][] tiles;
    TileType[][] originalTiles;

    int width = 15;
    int length = 15;

    Robot robot;

    @FXML
    Button btnForward;
    @FXML
    Button btnTurnLeft;
    @FXML
    Button btnTurnRight;
    @FXML
    Label labelMoves;
    @FXML
    Label labelShortest;
    @FXML
    ListView<String> listMoves;
    @FXML
    Button btnRun;
    @FXML
    Button btnReset;
    @FXML
    Button btnNewMaze;
    @FXML
    Button btnViewPath;

    Timeline timeline;

    @FXML
    private void initialize() {
        initializeGrid();
        initializeRobot();
        setGridColors();
        setActions();
    }

    @FXML
    private void initializeGrid() {
        tiles = MazeGeneration.generateMaze(15, 15);
        originalTiles = new TileType[tiles.length][];

        for (int i = 0; i < tiles.length; i++) {
            originalTiles[i] = tiles[i].clone();
        }

        btnRun.setDisable(false);
        btnReset.setDisable(false);
        btnNewMaze.setDisable(false);
        btnForward.setDisable(false);
        btnTurnLeft.setDisable(false);
        btnTurnRight.setDisable(false);
        btnViewPath.setDisable(false);
        setGridColors();
    }

    @FXML
    private void setGridColors() {
        grid.getChildren().clear();

        int rows = tiles.length;
        int cols = tiles[0].length;

        DoubleBinding cellSize = Bindings.createDoubleBinding(() -> {
            Insets insets = grid.getInsets();
            double availableWidth = grid.getWidth() - insets.getLeft() - insets.getRight();
            double availableHeight = grid.getHeight() - insets.getTop() - insets.getBottom();
            return Math.max(0, Math.floor(Math.min(availableWidth / cols, availableHeight / rows)));
        }, grid.widthProperty(), grid.heightProperty(), grid.insetsProperty());

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                TileType type = tiles[row][col];
                Rectangle cell = new Rectangle();
                cell.widthProperty().bind(cellSize);
                cell.heightProperty().bind(cellSize);
                cell.setStrokeType(StrokeType.INSIDE);

                switch (type) {
                    case WALL ->
                        cell.setFill(Color.DIMGRAY);
                    case EMPTY ->
                        cell.setFill(Color.LIGHTGRAY);
                    case ROBOT ->
                        cell.setFill(Color.DODGERBLUE);
                    case TARGET ->
                        cell.setFill(Color.LIMEGREEN);
                    default -> {
                    }
                }

                cell.setStroke(Color.BLACK);
                grid.add(cell, col, row);
            }
        }
    }

    @FXML
    private void initializeRobot() {
        int r = -1;
        int c = -1;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] == ROBOT) {
                    r = i;
                    c = j;
                    break;
                }
            }
            if (r != -1) {
                break;
            }
        }

        if (r == -1 || c == -1) {
            throw new IllegalStateException("Robot not found");
        }

        robot = new Robot(r, c);
        robot.setDirection("RIGHT");
    }

    @FXML
    private void setActions() {
        btnForward.setOnAction(e -> moves("FORWARD"));
        btnTurnLeft.setOnAction(e -> moves("TURN_LEFT"));
        btnTurnRight.setOnAction(e -> moves("TURN_RIGHT"));
        btnRun.setOnAction(e -> runMoves(listMoves.getItems()));
        btnReset.setOnAction(e -> reset());
        btnViewPath.setOnAction(e -> viewFastestPath());
        btnNewMaze.setOnAction(e -> generateMaze());
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

        btnRun.setDisable(false);
        btnReset.setDisable(false);
        btnNewMaze.setDisable(false);
        btnForward.setDisable(false);
        btnTurnLeft.setDisable(false);
        btnTurnRight.setDisable(false);
        btnViewPath.setDisable(false);

        setGridColors();
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

        labelMoves.setText("Moves: " + listMoves.getItems().size());
    }

    private void forward() {
        int oldRow = robot.getRow();
        int oldCol = robot.getCol();
        robot.move();

        switch (robot.getDirection()) {
            case "UP" ->
                moveUp();
            case "DOWN" ->
                moveDown();
            case "LEFT" ->
                moveLeft();
            case "RIGHT" ->
                moveRight();
            default -> {
            }
        }

        if (tiles[oldRow][oldCol] == ROBOT) {
            tiles[oldRow][oldCol] = originalTiles[oldRow][oldCol] == TARGET ? TARGET : EMPTY;
        }

        tiles[robot.getRow()][robot.getCol()] = ROBOT;
        setGridColors();
    }

    private void moveUp() {
        int row = robot.getRow();
        int col = robot.getCol();

        if (row > 0 && tiles[row - 1][col] != WALL) {
            tiles[row][col] = EMPTY;
            robot.setRow(row - 1);
        }
    }

    private void moveDown() {
        int row = robot.getRow();
        int col = robot.getCol();

        if (row < tiles.length - 1 && tiles[row + 1][col] != WALL) {
            tiles[row][col] = EMPTY;
            robot.setRow(row + 1);
        }
    }

    private void moveLeft() {
        int row = robot.getRow();
        int col = robot.getCol();

        if (col > 0 && tiles[row][col - 1] != WALL) {
            tiles[row][col] = EMPTY;
            robot.setCol(col - 1);
        }
    }

    private void moveRight() {
        int row = robot.getRow();
        int col = robot.getCol();

        if (col < tiles[row].length - 1 && tiles[row][col + 1] != WALL) {
            tiles[row][col] = EMPTY;
            robot.setCol(col + 1);
        }
    }

    private void turnLeft() {
        robot.move();

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
    }

    private void turnRight() {
        robot.move();

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
    }

    private void generateMaze() {
        Dialog<int[]> dialog = new Dialog<>();

        dialog.setTitle("Maze Settings");
        dialog.setHeaderText(
                "Enter the maze dimensions.\nRows and columns must be odd numbers."
        );
        dialog.setResizable(false);


        TextField rowsField = new TextField("15");
        TextField colsField = new TextField("15");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Rows:"), 0, 0);
        grid.add(rowsField, 1, 0);
        grid.add(new Label("Columns:"), 0, 1);
        grid.add(colsField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK, ButtonType.CANCEL
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
            int rows = dimensions[0];
            int cols = dimensions[1];

            tiles = MazeGeneration.generateMaze(rows, cols);
            originalTiles = new TileType[tiles.length][];

            for (int i = 0; i < tiles.length; i++) {
                originalTiles[i] = tiles[i].clone();
            }
            setGridColors();
        });
    }

    private void viewFastestPath() {
        Point start = getRobot(originalTiles);
        Point target = getTarget(originalTiles);

        List<Point> path = FastestPathAlg.shortestPath(originalTiles, start, target);

        setGridColors();

        if (path.isEmpty()) {
            labelShortest.setText("Shortest: no path");
            return;
        }

        labelShortest.setText("Shortest: "
                + FastestPathAlg.calculateMoves(path));

        for (Point point : path) {
            if ((point.x == start.x && point.y == start.y)
                    || (point.x == target.x && point.y == target.y)) {
                continue;
            }

            for (javafx.scene.Node node : grid.getChildren()) {
                Integer row = GridPane.getRowIndex(node);
                Integer col = GridPane.getColumnIndex(node);

                if (node instanceof Rectangle cell
                        && row != null && col != null
                        && row == point.x && col == point.y) {
                    cell.setFill(Color.RED);
                    break;
                }
            }
        }

        btnRun.setDisable(true);
        btnForward.setDisable(true);
        btnTurnLeft.setDisable(true);
        btnTurnRight.setDisable(true);
        btnViewPath.setDisable(true);
    }

    private Point getTarget(TileType[][] maze) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == TARGET) {
                    return new Point(i, j);
                }
            }
        }
        throw new IllegalStateException("Target not found");
    }

    private Point getRobot(TileType[][] maze) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == ROBOT) {
                    return new Point(i, j);
                }
            }
        }
        throw new IllegalStateException("Robot not found");
    }
}
