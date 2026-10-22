package com.robomaze.services;

import static com.robomaze.models.TileType.EMPTY;
import static com.robomaze.models.TileType.ROBOT;
import static com.robomaze.models.TileType.TARGET;

import com.robomaze.algorithms.FastestPathAlg;
import com.robomaze.models.Maze;
import com.robomaze.models.Robot;
import com.robomaze.models.TileType;
import java.awt.Point;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
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
     * Runs the robot's moves in the maze using a JavaFX Timeline to update its
     * position and orientation based on the commands.
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

        // Create a snapshot so the commands remain unchanged during playback.
        List<String> commands = List.copyOf(moves);

        if (commands.isEmpty()) {
            return null;
        }

        // Track which command should be executed in the next timeline cycle.
        final int[] index = {0};

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(250), event -> {
                    String move = commands.get(index[0]++);

                    switch (move) {
                        case "FORWARD":
                            forward(robot, grid, maze, originalMaze);

                            // Check for a win after each forward move.
                            boolean winStatus = checkWinStatus(maze, originalMaze);
                            if (winStatus) {
                                Dialog<String> dialog = new Dialog<>();
                                dialog.setTitle("Congratulations!");
                                dialog.setHeaderText("You won!");
                                dialog.setContentText("You completed the maze in " 
                                        + moves.size() + " moves.\n"
                                        + "Shortest number of moves: "
                                        + FastestPathAlg.calculateMoves(
                                                FastestPathAlg.shortestPath(
                                                        maze, 
                                                        getRobot(originalMaze), 
                                                        getTarget(originalMaze)
                                                )));
                                Button btnOk = new Button("OK");
                                btnOk.setOnAction(e -> dialog.close());
                                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                                btnOk.setDefaultButton(true);
                                Platform.runLater(dialog::showAndWait);
                                return;
                            }
                            break;
                        case "TURN_LEFT":
                            turnLeft(robot, grid, maze);
                            break;
                        case "TURN_RIGHT":
                            turnRight(robot, grid, maze);
                            break;
                        default:
                            break;
                    }
                })
        );

        // Execute one command every 250 milliseconds.
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

    /**
     * Checks if the robot has reached the target position in the maze.
     *
     * @param maze the maze to check the robot position in
     * @param originalMaze the original maze to get the target position from
     * @return true if the robot has reached the target position, false
     * otherwise
     */
    public static boolean checkWinStatus(Maze maze, Maze originalMaze) {
        return GameServices.getRobot(maze).equals(GameServices.getTarget(originalMaze));
    }
}
