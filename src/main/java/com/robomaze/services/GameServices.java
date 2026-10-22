
package com.robomaze.services;

import com.robomaze.models.Maze;
import com.robomaze.models.Robot;
import com.robomaze.models.TileType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import java.awt.Point;
import java.util.List;

import static com.robomaze.models.TileType.*;

public class GameServices {

    public static Robot initializeRobot(Maze maze) {
        Point position = getRobot(maze);

        Robot robot = new Robot(position.x, position.y);
        robot.setDirection("RIGHT");

        return robot;
    }

    public static Timeline runMoves(
            List<String> moves,
            Robot robot,
            GridPane grid,
            Maze maze,
            Maze originalMaze) {

        List<String> commands = List.copyOf(moves);

        if (commands.isEmpty()) {
            return null;
        }

        final int[] index = {0};

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(250), event -> {
                    String move = commands.get(index[0]++);

                    switch (move) {
                        case "FORWARD" ->
                                forward(robot, grid, maze, originalMaze);
                        case "TURN_LEFT" ->
                                turnLeft(robot, grid, maze);
                        case "TURN_RIGHT" ->
                                turnRight(robot, grid, maze);
                        default -> {
                        }
                    }
                })
        );

        timeline.setCycleCount(commands.size());
        timeline.play();

        return timeline;
    }

    public static void forward(
            Robot robot,
            GridPane grid,
            Maze maze,
            Maze originalMaze) {

        int oldRow = robot.getRow();
        int oldCol = robot.getCol();

        int newRow = oldRow;
        int newCol = oldCol;

        switch (robot.getDirection()) {
            case "UP" -> newRow--;
            case "DOWN" -> newRow++;
            case "LEFT" -> newCol--;
            case "RIGHT" -> newCol++;
            default -> {
            }
        }

        if (maze.isWalkable(newRow, newCol)) {
            // Restore the tile the robot is leaving.
            TileType originalTile =
                    originalMaze.getTile(oldRow, oldCol);

            maze.setTile(
                    oldRow,
                    oldCol,
                    originalTile == TARGET ? TARGET : EMPTY
            );

            // Update the robot's position.
            robot.setRow(newRow);
            robot.setCol(newCol);

            // Mark the robot's new position.
            maze.setTile(newRow, newCol, ROBOT);
        }

        MazeRenderer.renderMaze(List.of(), grid, maze, robot);
    }

    public static void turnLeft(
            Robot robot,
            GridPane grid,
            Maze maze) {

        switch (robot.getDirection()) {
            case "UP" -> robot.setDirection("LEFT");
            case "LEFT" -> robot.setDirection("DOWN");
            case "DOWN" -> robot.setDirection("RIGHT");
            case "RIGHT" -> robot.setDirection("UP");
            default -> {
            }
        }

        MazeRenderer.renderMaze(List.of(), grid, maze, robot);
    }

    public static void turnRight(
            Robot robot,
            GridPane grid,
            Maze maze) {

        switch (robot.getDirection()) {
            case "UP" -> robot.setDirection("RIGHT");
            case "RIGHT" -> robot.setDirection("DOWN");
            case "DOWN" -> robot.setDirection("LEFT");
            case "LEFT" -> robot.setDirection("UP");
            default -> {
            }
        }

        MazeRenderer.renderMaze(List.of(), grid, maze, robot);
    }

    public static Point getTarget(Maze maze) {
        for (int row = 0; row < maze.getRows(); row++) {
            for (int col = 0; col < maze.getCols(); col++) {
                if (maze.getTile(row, col) == TARGET) {
                    return new Point(row, col);
                }
            }
        }

        throw new IllegalStateException("Target not found");
    }

    public static Point getRobot(Maze maze) {
        for (int row = 0; row < maze.getRows(); row++) {
            for (int col = 0; col < maze.getCols(); col++) {
                if (maze.getTile(row, col) == ROBOT) {
                    return new Point(row, col);
                }
            }
        }

        throw new IllegalStateException("Robot not found");
    }
}
