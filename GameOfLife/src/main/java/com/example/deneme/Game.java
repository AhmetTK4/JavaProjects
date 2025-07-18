package com.example.deneme;

public class Game {

    public void applyBirthRule(Cell cell) {
        if (!cell.isAlive() && cell.getNeighbours() == 3) {
            cell.setAlive(true);
        } else {
            cell.setAlive(false);
        }
    }

    public void applySurvivalRule(Cell cell) {
        if (cell.getNeighbours() == 2 || cell.getNeighbours() == 3) {
            cell.setAlive(true);
        } else {
            cell.setAlive(false);
        }
    }

    private Grid grid;

    public Game(int rows, int cols) {
        grid = new Grid(rows, cols);
    }

    public void runSimulation(int generations) {
        for (int i = 0; i < generations; i++) {
            System.out.println("Generation " + (i + 1));
            grid.displayGrid();
            grid.updateCells();
        }
    }
}
