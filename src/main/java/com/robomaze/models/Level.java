package com.robomaze.models;

import java.awt.*;

public class Level {

    private Grid grid;
    private Robot robot;
    private int levelNumber;
    private int targetRow;
    private int targetCol;

    public Level(Grid grid, Robot robot, int levelNumber, int targetRow, int targetCol) {
        this.grid = grid;
        this.robot = robot;
        this.levelNumber = levelNumber;
        this.targetRow = targetRow;
        this.targetCol = targetCol;
    }

    public Grid getGrid() {
        return grid;
    }

    public Robot getRobot() {
        return robot;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public int getTargetRow() {
        return targetRow;
    }

    public int getTargetCol() {
        return targetCol;
    }

    public Point getTarget() {
        return new Point(targetRow, targetCol);
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public void setTargetRow(int targetRow) {
        this.targetRow = targetRow;
    }

    public void setTargetCol(int targetCol) {
        this.targetCol = targetCol;
    }

    public void setTarget(Point target) {
        this.targetRow = target.x;
        this.targetCol = target.y;
    }

}
