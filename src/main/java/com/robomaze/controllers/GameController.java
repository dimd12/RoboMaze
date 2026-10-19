package com.robomaze.controllers;

import static com.robomaze.models.TileType.EMPTY;
import static com.robomaze.models.TileType.ROBOT;
import static com.robomaze.models.TileType.TARGET;
import static com.robomaze.models.TileType.WALL;

import com.robomaze.models.Robot;
import com.robomaze.models.TileType;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
    ListView<String> listMoves;

    @FXML
    Button btnRun;

    @FXML
    Button btnReset;

    @FXML
    Button btnNewMaze;

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
        tiles = new TileType[][]{
            // Row 0
            {WALL, WALL, WALL, WALL, WALL, WALL, WALL,
                WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL},
            // Row 1
            {WALL, ROBOT, EMPTY, EMPTY, WALL, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, WALL, EMPTY, EMPTY, EMPTY, WALL},
            // Row 2
            {WALL, WALL, WALL, EMPTY, WALL, EMPTY, WALL,
                WALL, WALL, EMPTY, WALL, EMPTY, WALL, EMPTY, WALL},
            // Row 3
            {WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL, EMPTY, WALL},
            // Row 4
            {WALL, EMPTY, WALL, WALL, WALL, WALL, WALL,
                EMPTY, WALL, WALL, WALL, EMPTY, WALL, EMPTY, WALL},
            // Row 5
            {WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL},
            // Row 6
            {WALL, WALL, WALL, EMPTY, WALL, WALL, WALL,
                EMPTY, WALL, EMPTY, WALL, WALL, WALL, EMPTY, WALL},
            // Row 7
            {WALL, EMPTY, EMPTY, EMPTY, WALL, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL, EMPTY, WALL},
            // Row 8
            {WALL, EMPTY, WALL, EMPTY, WALL, EMPTY, WALL,
                WALL, WALL, WALL, WALL, EMPTY, WALL, EMPTY, WALL},
            // Row 9
            {WALL, EMPTY, WALL, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, EMPTY, WALL, EMPTY, EMPTY, EMPTY, WALL},
            // Row 10
            {WALL, EMPTY, WALL, WALL, WALL, WALL, WALL,
                WALL, WALL, EMPTY, WALL, WALL, WALL, EMPTY, WALL},
            // Row 11
            {WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                EMPTY, EMPTY, WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL},
            // Row 12
            {WALL, WALL, WALL, WALL, WALL, EMPTY, WALL,
                EMPTY, WALL, WALL, WALL, WALL, WALL, EMPTY, WALL},
            // Row 13
            {WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL,
                EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, TARGET, WALL},
            // Row 14
            {WALL, WALL, WALL, WALL, WALL, WALL, WALL,
                WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL}
        };
        originalTiles = new TileType[tiles.length][];

        for (int i = 0; i < tiles.length; i++) {
            originalTiles[i] = tiles[i].clone();
        }
    }

    @FXML
    private void setGridColors() {
        grid.getChildren().clear();

        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[row].length; col++) {
                TileType type = tiles[row][col];
                Rectangle cell = new Rectangle(30, 30);

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
        int r = 0;
        int c = 0;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] == ROBOT) {
                    r = i;
                    c = j;
                }
            }
        }

        robot = new Robot(r, c);
        robot.setDirection("UP");
    }

    @FXML
    private void setActions() {
        btnForward.setOnAction(e -> moves("FORWARD"));
        btnTurnLeft.setOnAction(e -> moves("TURN_LEFT"));
        btnTurnRight.setOnAction(e -> moves("TURN_RIGHT"));
        btnRun.setOnAction(e -> runMoves(listMoves.getItems()));
        btnReset.setOnAction(e -> reset());
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

        setGridColors();
    }

    private void runMoves(List<String> moves) {
        if (timeline != null) {
            timeline.stop();
        }

        List<String> commands = List.copyOf(moves);
        final int[] index = {0};

        timeline = new Timeline(
                new KeyFrame(Duration.millis(250), event -> {
                    if (index[0] < commands.size()) {
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
}
