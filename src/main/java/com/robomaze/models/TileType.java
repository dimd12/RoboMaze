package com.robomaze.models;

/**
 * Enum representing the different types of tiles in the maze.
 *
 * @author Adrian Manoil
 * @id 2503832
 * @author Sofia Martins Castro e Moura
 * @id 2476843
 */
public enum TileType {
    EMPTY,  // Walkable tile
    WALL,   // Unwalkable tile
    TARGET, // Target tile to be reached by the robot
    ROBOT   // Robot tile representing the robot's position
}
