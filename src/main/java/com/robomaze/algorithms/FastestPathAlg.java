package com.robomaze.algorithms;

import com.robomaze.models.Maze;
import com.robomaze.models.TileType;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Finds the shortest path with the smallest number of tiles
 * through a maze and calculates its movement cost.
 *
 * @author Adrian Manoil
 * @id 2503832
 * @author Sofia Martins Castro e Moura
 * @id 2476843
 */
public class FastestPathAlg {

    private static final int[] DIRECTIONS_ROWS = {-1, 1, 0, 0};
    private static final int[] DIRECTIONS_COLS = {0, 0, -1, 1};

    /**
     * Finds the shortest path using a Breadth-First Search (BFS) algorithm.
     * It uses a queue to explore neighboring points from the start point
     * until the end point is reached and adds the position of the
     * shortest number of tiles to be covered.
     *
     * @param maze the maze to search
     * @param start the starting point
     * @param end the destination point
     * @return the shortest path, or an empty list if no path exists
     * @see Maze
     */
    public static List<Point> shortestPath(
            Maze maze, Point start, Point end) {

        int rows = maze.getRows();
        int cols = maze.getCols();

        boolean[][] visited = new boolean[rows][cols];
        Point[][] previous = new Point[rows][cols];
        Queue<Point> queue = new LinkedList<>();

        queue.add(start);
        visited[start.x][start.y] = true;

        // The loop is used to continue searching for the shortest path
        // until the queue is empty by removing the current point from the queue.
        while (!queue.isEmpty()) {
            Point current = queue.poll();

            if (current.x == end.x && current.y == end.y) {
                break;
            }

            // Check all four neighboring tiles.
            for (int i = 0; i < 4; i++) {
                int newRow = current.x + DIRECTIONS_ROWS[i];
                int newCol = current.y + DIRECTIONS_COLS[i];

                if (newRow < 0 || newRow >= rows
                        || newCol < 0 || newCol >= cols) {
                    continue;
                }

                if (maze.getTile(newRow, newCol) == TileType.WALL
                        || visited[newRow][newCol]) {
                    continue;
                }

                visited[newRow][newCol] = true;
                previous[newRow][newCol] = current;
                // Add the new point to the queue to be explored.
                queue.add(new Point(newRow, newCol)); 
            }
        }

        if (!visited[end.x][end.y]) {
            return Collections.emptyList();
        }

        // Reconstruct the path by following predecessors from end to start.
        List<Point> path = new ArrayList<>();
        Point current = end;

        while (current != null) {
            path.add(current);
            current = previous[current.x][current.y];
        }

        // Reverse the path to get it from start to end.
        Collections.reverse(path);
        return path;
    }

    /**
     * Counts the direction changes in a path.
     *
     * @param path the path to check
     * @return the number of direction changes
     */
    public static int calculateDirChanges(List<Point> path) {
        if (path == null || path.size() < 3) {
            return 0;
        }

        int dirChanges = 0;

        // Checks direction changes on horizontal and vertical directions
        // from the path, checking 3 consecutive points
        for (int i = 1; i < path.size() - 1; i++) {
            Point previous = path.get(i - 1);
            Point current = path.get(i);
            Point next = path.get(i + 1);

            boolean firstIsHorizontal = previous.x == current.x;
            boolean secondIsHorizontal = current.x == next.x;

            if (firstIsHorizontal != secondIsHorizontal) {
                dirChanges++;
            }
        }

        return dirChanges;
    }

    /**
     * Calculates the total moves, counting each step and direction change.
     *
     * @param path the path to check
     * @return the number of steps plus direction changes
     * @see #calculateDirChanges(List)
     */
    public static int calculateMoves(List<Point> path) {
        if (path == null || path.isEmpty()) {
            return 0;
        }

        return path.size() - 1 + calculateDirChanges(path);
    }
}
