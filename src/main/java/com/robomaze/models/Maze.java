package com.robomaze.models;

public class Maze {

    private final TileType[][] tiles;

    public Maze(TileType[][] tiles) {
        this.tiles = copyGrid(tiles);
    }

    public int getRows() {
        return tiles.length;
    }

    public int getCols() {
        return tiles[0].length;
    }

    public TileType getTile(int row, int col) {
        return tiles[row][col];
    }

    public void setTile(int row, int col, TileType type) {
        tiles[row][col] = type;
    }

    public boolean isInside(int row, int col) {
        return row >= 0 && row < getRows()
                && col >= 0 && col < getCols();
    }

    public boolean isWalkable(int row, int col) {
        return isInside(row, col)
                && tiles[row][col] != TileType.WALL;
    }

    public TileType[][] getTilesCopy() {
        return copyGrid(tiles);
    }

    private TileType[][] copyGrid(TileType[][] source) {
        TileType[][] copy = new TileType[source.length][];

        if (source.length == 0) {
            return copy;
        }

        for (int row = 0; row < source.length; row++) {
            copy[row] = source[row].clone();
        }

        return copy;
    }
}
