package com.robomaze.models;

import java.awt.Point;

/**
 * Represents the robot in the maze.
 *
 * @author Adrian Manoil
 * @id 2503832
 * @author Sofia Martins Castro e Moura
 * @id 2476843
 */
public class Robot {

    private int row;
    private int col;
    private int moves;
    private String direction;

    /**
     * Constructs a new Robot object with the specified row and column,
     * resetting the number of moves to zero and setting the initial direction
     * to "RIGHT".
     *
     * @param row the row position of the robot
     * @param col the column position of the robot
     */
    public Robot(int row, int col) {
        this.row = row;
        this.col = col;
        this.moves = 0;
        this.direction = "RIGHT";
    }

    /**
     * Returns the row position of the robot.
     *
     * @return the row position of the robot
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column position of the robot.
     *
     * @return the column position of the robot
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns the number of moves made by the robot.
     *
     * @return the number of moves made by the robot
     */
    public int getMoves() {
        return moves;
    }

    /**
     * Returns the direction of the robot.
     *
     * @return the direction of the robot
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the row position of the robot.
     *
     * @param row the new row position of the robot
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Sets the column position of the robot.
     *
     * @param col the new column position of the robot
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Sets the direction of the robot.
     *
     * @param direction the new direction of the robot
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Moves the robot in the current direction.
     */
    public void move() {
        this.moves++;
    }

}
