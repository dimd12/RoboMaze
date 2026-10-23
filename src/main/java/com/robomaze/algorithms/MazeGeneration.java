package com.robomaze.algorithms;

import com.robomaze.models.Maze;
import com.robomaze.models.TileType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * Generates a maze using randomized depth-first search. All tiles are initially
 * walls, and passages are carved by exploring unvisited cells in a randomized
 * order. The outer boundary remains closed, with the robot placed at the
 * starting cell and the target placed in the opposite interior corner.
 *
 * @author Adrian Manoil
 * @id 2503832
 * @author Sofia Martins Castro e Moura
 * @id 2476843
 */
public class MazeGeneration {

    private static final Random RANDOM = new Random();

    // Directions for moving between cells, skipping the walls between them.
    private static final int[][] DIRECTIONS = {
        {-2, 0},
        {2, 0},
        {0, -2},
        {0, 2}
    };

    /**
     * Generates a maze with a robot starting position and a target using
     * a Depth-First Search (DFS) algorithm. It sets the exterior boundary
     * of the maze to walls, leaving the interior open for the robot and target.
     * It starts from the (1, 1) position and explores the maze by randomly choosing
     * a direction and removing the wall between the current cell and the next.
     *
     * @param rows the number of rows
     * @param cols the number of columns
     * @return the generated maze
     * @throws IllegalArgumentException if the dimensions are invalid
     * @see Maze
     */
    public static Maze generateMaze(int rows, int cols) {
        if (rows < 5 || cols < 5
                || rows % 2 == 0 || cols % 2 == 0) {
            throw new IllegalArgumentException(
                    "Maze dimensions must be odd and at least 5."
            );
        }

        // Initialize the maze with walls.
        TileType[][] tiles = new TileType[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                tiles[row][col] = TileType.WALL;
            }
        }

        boolean[][] visited = new boolean[rows][cols];
        Stack<int[]> stack = new Stack<>();

        // Start generating the maze from the top-left interior cell.
        int startRow = 1;
        int startCol = 1;

        visited[startRow][startCol] = true;
        tiles[startRow][startCol] = TileType.EMPTY;
        stack.push(new int[]{startRow, startCol});

        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int row = current[0];
            int col = current[1];

            List<int[]> neighbours = new ArrayList<>();

            // Find unvisited neighboring cells inside the maze.
            for (int[] direction : DIRECTIONS) {
                int nextRow = row + direction[0];
                int nextCol = col + direction[1];

                if (nextRow > 0 && nextRow < rows - 1
                        && nextCol > 0 && nextCol < cols - 1
                        && !visited[nextRow][nextCol]) {
                    neighbours.add(new int[]{nextRow, nextCol});
                }
            }

            if (!neighbours.isEmpty()) {
                // Choose a random neighbor and remove the wall between them.
                Collections.shuffle(neighbours, RANDOM);

                int[] next = neighbours.getFirst();
                int nextRow = next[0];
                int nextCol = next[1];

                int wallRow = (row + nextRow) / 2;
                int wallCol = (col + nextCol) / 2;

                tiles[wallRow][wallCol] = TileType.EMPTY;
                tiles[nextRow][nextCol] = TileType.EMPTY;

                visited[nextRow][nextCol] = true;
                stack.push(next);
            } else {
                // Backtrack when there are no unvisited neighbors.
                stack.pop();
            }
        }

        // Keep the outer boundary closed.
        for (int row = 0; row < rows; row++) {
            tiles[row][0] = TileType.WALL;
            tiles[row][cols - 1] = TileType.WALL;
        }

        for (int col = 0; col < cols; col++) {
            tiles[0][col] = TileType.WALL;
            tiles[rows - 1][col] = TileType.WALL;
        }

        // Set the robot's starting position and the target.
        tiles[1][1] = TileType.ROBOT;
        tiles[rows - 2][cols - 2] = TileType.TARGET;

        return new Maze(tiles);
    }
}
