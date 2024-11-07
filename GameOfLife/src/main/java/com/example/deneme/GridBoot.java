package com.example.deneme;

import java.util.Random;

public class GridBoot {
    private CellBoot[][] cells;
    private int rows;
    private int cols;

    public GridBoot(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        cells = new CellBoot[rows][cols];
        initializeRandomPattern();
    }

    // Rastgele başlat
    private void initializeRandomPattern() {
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new CellBoot(random.nextDouble() < 0.3);
            }
        }
    }

    // Her nesilde hücreleri güncelle
    public void updateCells() {
        CellBoot[][] newCells = new CellBoot[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int neighbours = countAliveNeighbours(i, j);
                boolean isAlive = cells[i][j].isAlive();

                if (isAlive && (neighbours < 2 || neighbours > 3)) {
                    newCells[i][j] = new CellBoot(false); // Ölüyor
                } else if (!isAlive && neighbours == 3) {
                    newCells[i][j] = new CellBoot(true); // Doğuyor
                } else {
                    newCells[i][j] = new CellBoot(isAlive); // Durumu korur
                }
            }
        }

        cells = newCells; // Yeni nesil hücreleri güncelle
    }

    // Canlı komşuları say
    private int countAliveNeighbours(int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int nx = x + i, ny = y + j;
                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && cells[nx][ny].isAlive()) {
                    count++;
                }
            }
        }
        return count;
    }

    public CellBoot[][] getCells() {
        return cells;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }
}
