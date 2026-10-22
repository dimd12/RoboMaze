package com.robomaze.models;

/**
 * Represents a maze.
 *
 * @author Adrian Manoil
 * @id 2503832
 * @author Sofia Martins Castro e Moura
 * @id 2476843
 */
public class Maze {

    private final TileType[][] tiles;

    /**
     * Constructs a new Maze object.
     *
     * @param tiles the tiles of the maze
     */
    public Maze(TileType[][] tiles) {
        this.tiles = copyGrid(tiles);
    }

    /**
     * Returns the number of rows in the maze.
     *
     * @return the number of rows in the maze
     */
    public int getRows() {
        return tiles.length;
    }

    /**
     * Returns the number of columns in the maze.
     *
     * @return the number of columns in the maze
     */
    public int getCols() {
        return tiles[0].length;
    }

    /**
     * Returns the tile at the specified row and column.
     *
     * @param row the row of the tile
     * @param col the column of the tile
     * @return the tile at the specified row and column
     */
    public TileType getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Sets the tile at the specified row and column.
     *
     * @param row the row of the tile
     * @param col the column of the tile
     * @param type the type of the tile
     */
    public void setTile(int row, int col, TileType type) {
        tiles[row][col] = type;
    }

    /**
     * Returns whether the specified row and column are inside the maze.
     *
     * @param row the row of the tile
     * @param col the column of the tile
     * @return true if the specified row and column are inside the maze, false otherwise
     */
    public boolean isInside(int row, int col) {
        return row >= 0 && row < getRows()
                && col >= 0 && col < getCols();
    }

    /**
     * Returns whether the specified row and column are walkable.
     *
     * @param row the row of the tile
     * @param col the column of the tile
     * @return true if the specified row and column are walkable, false otherwise
     */
    public boolean isWalkable(int row, int col) {
        return isInside(row, col)
                && tiles[row][col] != TileType.WALL;
    }

    /**
     * Returns a copy of the maze's tiles.
     *
     * @return a copy of the maze's tiles
     */
    public TileType[][] getTilesCopy() {
        return copyGrid(tiles);
    }

    /**
     * Returns a copy of the grid
     * formed by the maze's tiles.
     *
     * @return a copy of the grid
     */
    private TileType[][] copyGrid(TileType[][] source) {
        TileType[][] copy = new TileType[source.length][];

        for (int row = 0; row < source.length; row++) {
            copy[row] = source[row].clone();
        }

        return copy;
    }

    /**
     * Returns a copy of the maze.
     *
     * @return a copy of the maze
     */
    public Maze copy () {
        return new Maze(tiles);
    }
}
