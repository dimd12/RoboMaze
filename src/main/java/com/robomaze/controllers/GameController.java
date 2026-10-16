package com.robomaze.controllers;

import com.robomaze.App;
import java.io.IOException;

import com.robomaze.models.Grid;
import com.robomaze.models.TileType;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.robomaze.models.TileType.*;

public class GameController {

    @FXML
    GridPane grid;

    @FXML
    Grid gridItems;

    TileType[][] tiles;
    int width = 15;
    int length = 15;

    @FXML
    private void initialize() {
        initializeGrid();
    }

    @FXML
    private void initializeGrid() {
        tiles = new TileType[15][15];
        TileType[][] tiles = {
                // Row 0
                {WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL},
                // Row 1
                {WALL, ROBOT, EMPTY, EMPTY, WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL, EMPTY, EMPTY, EMPTY, WALL},
                // Row 2
                {WALL, WALL, WALL, EMPTY, WALL, EMPTY, WALL, WALL, WALL, EMPTY, WALL, EMPTY, WALL, EMPTY, WALL},
                // Row 3
                {WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL, EMPTY, WALL},
                // Row 4
                {WALL, EMPTY, WALL, WALL, WALL, WALL, WALL, EMPTY, WALL, WALL, WALL, EMPTY, WALL, EMPTY, WALL},
                // Row 5
                {WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL},
                // Row 6
                {WALL, WALL, WALL, EMPTY, WALL, WALL, WALL, EMPTY, WALL, EMPTY, WALL, WALL, WALL, EMPTY, WALL},
                // Row 7
                {WALL, EMPTY, EMPTY, EMPTY, WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL, EMPTY, WALL},
                // Row 8
                {WALL, EMPTY, WALL, EMPTY, WALL, EMPTY, WALL, WALL, WALL, WALL, WALL, EMPTY, WALL, EMPTY, WALL},
                // Row 9
                {WALL, EMPTY, WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL, EMPTY, EMPTY, EMPTY, WALL},
                // Row 10
                {WALL, EMPTY, WALL, WALL, WALL, WALL, WALL, WALL, WALL, EMPTY, WALL, WALL, WALL, EMPTY, WALL},
                // Row 11
                {WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL},
                // Row 12
                {WALL, WALL, WALL, WALL, WALL, EMPTY, WALL, EMPTY, WALL, WALL, WALL, WALL, WALL, EMPTY, WALL},
                // Row 13
                {WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WALL, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, TARGET, WALL},
                // Row 14
                {WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL, WALL}
        };

        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[row].length; col++) {
                TileType type = tiles[row][col];

                Rectangle cell = new Rectangle(30, 30);

                switch (type) {
                    case WALL -> cell.setFill(Color.DIMGRAY);
                    case EMPTY -> cell.setFill(Color.LIGHTGRAY);
                    case ROBOT -> cell.setFill(Color.DODGERBLUE);
                    case TARGET -> cell.setFill(Color.LIMEGREEN);
                }

                cell.setStroke(Color.BLACK);
                grid.add(cell, col, row);
            }
        }
    }
}