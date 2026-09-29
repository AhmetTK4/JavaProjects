package com.example.gameoflife;

import java.io.Serializable;
import java.util.Random;

/**
 * Immutable generation of a bounded (non-wrapping) Game of Life board.
 */
public final class Grid implements Serializable {

    private static final long serialVersionUID = 1L;

    private final boolean[][] cells;
    private final int rows;
    private final int cols;

    public Grid(boolean[][] cells) {
        if (cells.length == 0 || cells[0].length == 0) {
            throw new IllegalArgumentException("Grid must have at least one cell");
        }
        this.rows = cells.length;
        this.cols = cells[0].length;
        this.cells = new boolean[rows][];
        for (int i = 0; i < rows; i++) {
            if (cells[i].length != cols) {
                throw new IllegalArgumentException("All rows must have the same length");
            }
            this.cells[i] = cells[i].clone();
        }
    }

    /**
     * Creates a grid where each cell is alive with the given probability.
     */
    public static Grid random(int rows, int cols, double density, Random random) {
        boolean[][] cells = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = random.nextDouble() < density;
            }
        }
        return new Grid(cells);
    }

    public Grid next() {
        boolean[][] nextCells = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                nextCells[i][j] = GameRules.nextState(cells[i][j], countAliveNeighbours(i, j));
            }
        }
        return new Grid(nextCells);
    }

    int countAliveNeighbours(int row, int col) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int r = row + dx;
                int c = col + dy;
                if (r >= 0 && r < rows && c >= 0 && c < cols && cells[r][c]) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean[][] cells() {
        boolean[][] copy = new boolean[rows][];
        for (int i = 0; i < rows; i++) {
            copy[i] = cells[i].clone();
        }
        return copy;
    }

    public int rows() {
        return rows;
    }

    public int cols() {
        return cols;
    }

    /** Renders the grid as text, {@code O} for live and {@code .} for dead cells. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (boolean[] row : cells) {
            for (boolean alive : row) {
                sb.append(alive ? 'O' : '.');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
