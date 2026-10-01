package com.robomaze.models;

import java.awt.*;

public class Robot {

    private int row;
    private int col;
    private int moves;
    private String direction;

    public Robot(int row, int col) {
        this.row = row;
        this.col = col;
        this.moves = 0;
        this.direction = "LEFT";
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Point getPos() {
        return new Point(row, col);
    }

    public int getMoves() {
        return moves;
    }

    public String getDirection() {
        return direction;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setPos(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void move() {
        this.moves++;
    }

}
