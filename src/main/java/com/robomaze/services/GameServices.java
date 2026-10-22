package com.robomaze.services;

import static com.robomaze.models.TileType.EMPTY;
import static com.robomaze.models.TileType.ROBOT;
import static com.robomaze.models.TileType.TARGET;

import com.robomaze.models.Maze;
import com.robomaze.models.Robot;
import com.robomaze.models.TileType;
import java.awt.Point;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 * GameServices class, containing the methods responsible for the game logic.
 *
 * @author Adrian Manoil
 * @id 2503832
 * @author Sofia Martins Castro e Moura
 * @id 2476843
 */
public class GameServices {

    /**
     * Initializes the robot instance in the maze.
     *
     * @param maze the maze to initialize the robot in
     * @return the initialized robot
     */
    public static Robot initializeRobot(Maze maze) {
        Point position = getRobot(maze);

        Robot robot = new Robot(position.x, position.y);
        robot.setDirection("RIGHT");

        return robot;
    }

    /**
     * Runs the moves of the robot in the maze, using the Timeline javaFX object
     * to make the robot change its position and orientation based on the
     * commands.
     *
     * @param moves the moves to run
     * @param robot the robot to run the moves for
     * @param grid the GridPane to render the maze in
     * @param maze the maze to run the moves in
     * @param originalMaze the original maze to compare with
     * @return the Timeline of the moves
     */
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

    /**
     * Moves the robot forward in the maze by getting the initial position of
     * the robot and updating its position in the maze, checking if the next
     * tile is walkable.
     *
     * @param robot the robot to move
     * @param grid the GridPane to render the maze in
     * @param maze the maze to move the robot in
     * @param originalMaze the original maze to compare with
     */
    public static void forward(
            Robot robot,
            GridPane grid,
            Maze maze,
            Maze originalMaze) {

        robot.move();

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

        if (maze.isWalkable(newRow, newCol)) {
            TileType originalTile
                    = originalMaze.getTile(oldRow, oldCol);

            maze.setTile(
                    oldRow,
                    oldCol,
                    originalTile == TARGET ? TARGET : EMPTY
            );

            robot.setRow(newRow);
            robot.setCol(newCol);

            maze.setTile(newRow, newCol, ROBOT);
        }

        MazeRenderer.renderMaze(List.of(), grid, maze, robot);
    }

    /**
     * Turns the robot left in the maze by updating its direction.
     *
     * @param robot the robot to turn
     * @param grid the GridPane to render the maze in
     * @param maze the maze to turn the robot in
     */
    public static void turnLeft(
            Robot robot,
            GridPane grid,
            Maze maze) {

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

        MazeRenderer.renderMaze(List.of(), grid, maze, robot);
    }

    /**
     * Turns the robot right in the maze by updating its direction.
     *
     * @param robot the robot to turn
     * @param grid the GridPane to render the maze in
     * @param maze the maze to turn the robot in
     */
    public static void turnRight(
            Robot robot,
            GridPane grid,
            Maze maze) {

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

        MazeRenderer.renderMaze(List.of(), grid, maze, robot);
    }

    /**
     * Gets the target position in the maze.
     *
     * @param maze the maze to get the target position in
     * @return the target position
     */
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

    /**
     * Gets the robot position in the maze.
     *
     * @param maze the maze to get the robot position in
     * @return the robot position
     */
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
