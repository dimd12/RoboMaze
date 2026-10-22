package com.robomaze.algorithms;

import com.robomaze.models.Maze;
import com.robomaze.models.TileType;
import java.awt.Point;
import java.util.*;

public class FastestPathAlg {

    public static final int[] DIRECTIONS_ROWS = {-1, 1, 0, 0};
    public static final int[] DIRECTIONS_COLS = {0, 0, -1, 1};

    public static List<Point> shortestPath(
            Maze maze, Point start, Point end) {

        int rows = maze.getRows();
        int cols = maze.getCols();

        boolean[][] visited = new boolean[rows][cols];
        Point[][] previous = new Point[rows][cols];

        Queue<Point> queue = new LinkedList<>();

        queue.add(start);
        visited[start.x][start.y] = true;

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            if (current.x == end.x && current.y == end.y) {
                break;
            }

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
                queue.add(new Point(newRow, newCol));
            }
        }

        if (!visited[end.x][end.y]) {
            return Collections.emptyList();
        }

        List<Point> path = new ArrayList<>();
        Point current = end;

        while (current != null) {
            path.add(current);
            current = previous[current.x][current.y];
        }

        Collections.reverse(path);
        return path;
    }

    public static int calculateDirChanges(List<Point> path) {
        if (path == null || path.size() < 3) {
            return 0;
        }

        int dirChanges = 0;

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

    public static int calculateMoves(List<Point> path) {
        if (path == null || path.isEmpty()) {
            return 0;
        }

        return path.size() - 1 + calculateDirChanges(path);
    }
}
