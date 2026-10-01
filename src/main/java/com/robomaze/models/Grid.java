package com.robomaze.models;

public class Grid {

    private TileType[][] tiles;
    private int width;
    private int length;

    public Grid(TileType[][] tiles, int width, int length) {
        this.tiles = tiles;
        this.width = width;
        this.length = length;
    }

    public TileType[][] getTiles() {
        return tiles;
    }

    public void setTiles(TileType[][] tiles) {
        this.tiles = tiles;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

}
