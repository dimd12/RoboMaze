
package com.robomaze.controllers;

import com.robomaze.algorithms.FastestPathAlg;
import com.robomaze.algorithms.MazeGeneration;
import com.robomaze.models.Maze;
import com.robomaze.models.Robot;
import com.robomaze.services.GameServices;
import com.robomaze.services.MazeRenderer;
import java.awt.Point;
import java.util.List;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

public class GameController {

    @FXML
    private GridPane grid;

    private Robot robot;
    private Maze maze;

    public Maze originalMaze;

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
        originalMaze = MazeGeneration.generateMaze(15, 15);

        setActions();
        reset();
    }

    private void setActions() {
        btnForward.setOnAction(e -> addMove("FORWARD"));
        btnTurnLeft.setOnAction(e -> addMove("TURN_LEFT"));
        btnTurnRight.setOnAction(e -> addMove("TURN_RIGHT"));
        btnRun.setOnAction(e -> runMoves());
        btnReset.setOnAction(e -> reset());
        btnViewPath.setOnAction(e -> viewFastestPath());
        btnNewMaze.setOnAction(e -> MazeRenderer.generateMaze(this));
    }

    private void addMove(String move) {
        listMoves.getItems().add(move);
        labelMoves.setText("Moves: " + listMoves.getItems().size());
    }

    private void runMoves() {
        stopTimeline();

        timeline = GameServices.runMoves(
                listMoves.getItems(),
                robot,
                grid,
                maze,
                originalMaze
        );
    }

    private void stopTimeline() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }

    public void reset() {
        stopTimeline();

        maze = originalMaze.copy();
        robot = GameServices.initializeRobot(maze);

        listMoves.getItems().clear();
        labelMoves.setText("Moves: 0");
        labelShortest.setText("Shortest: -");

        enableButtons();
        MazeRenderer.renderMaze(List.of(), grid, maze, robot);
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

    private void viewFastestPath() {
        Point start = GameServices.getRobot(originalMaze);
        Point target = GameServices.getTarget(originalMaze);

        List<Point> path = FastestPathAlg.shortestPath(
                originalMaze,
                start,
                target
        );

        if (path.isEmpty()) {
            labelShortest.setText("Shortest: no path");
            return;
        }

        labelShortest.setText(
                "Shortest: " + FastestPathAlg.calculateMoves(path)
        );

        MazeRenderer.renderMaze(path, grid, maze, robot);

        btnRun.setDisable(true);
        btnForward.setDisable(true);
        btnTurnLeft.setDisable(true);
        btnTurnRight.setDisable(true);
        btnViewPath.setDisable(true);
    }
}
