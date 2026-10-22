package com.robomaze.algorithms;

import com.robomaze.models.Maze;
import com.robomaze.models.TileType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class MazeGeneration {

    private static final Random RANDOM = new Random();

    private static final int[][] DIRECTIONS = {
            {-2, 0},
            {2, 0},
            {0, -2},
            {0, 2}
    };

    public static Maze generateMaze(int rows, int cols) {
        if (rows < 5 || cols < 5
                || rows % 2 == 0 || cols % 2 == 0) {
            throw new IllegalArgumentException(
                    "Maze dimensions must be odd and at least 5."
            );
        }

        TileType[][] tiles = new TileType[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                tiles[row][col] = TileType.WALL;
            }
        }

        boolean[][] visited = new boolean[rows][cols];
        Stack<int[]> stack = new Stack<>();

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
                Collections.shuffle(neighbours, RANDOM);

                int[] next = neighbours.get(0);
                int nextRow = next[0];
                int nextCol = next[1];

                int wallRow = (row + nextRow) / 2;
                int wallCol = (col + nextCol) / 2;

                tiles[wallRow][wallCol] = TileType.EMPTY;
                tiles[nextRow][nextCol] = TileType.EMPTY;

                visited[nextRow][nextCol] = true;
                stack.push(next);
            } else {
                stack.pop();
            }
        }

        for (int row = 0; row < rows; row++) {
            tiles[row][0] = TileType.WALL;
            tiles[row][cols - 1] = TileType.WALL;
        }

        for (int col = 0; col < cols; col++) {
            tiles[0][col] = TileType.WALL;
            tiles[rows - 1][col] = TileType.WALL;
        }

        tiles[1][1] = TileType.ROBOT;
        tiles[rows - 2][cols - 2] = TileType.TARGET;

        return new Maze(tiles);
    }
}
