package com.example.gameoflife;

/**
 * Conway's rules in one place, used by every grid implementation.
 */
public final class GameRules {

    private GameRules() {
    }

    /**
     * @param alive          whether the cell is alive in the current generation
     * @param liveNeighbours number of live cells among its eight neighbours
     * @return whether the cell is alive in the next generation
     */
    public static boolean nextState(boolean alive, int liveNeighbours) {
        if (alive) {
            // Survival: two or three neighbours; otherwise under- or overpopulation.
            return liveNeighbours == 2 || liveNeighbours == 3;
        }
        // Birth: exactly three neighbours.
        return liveNeighbours == 3;
    }
}
