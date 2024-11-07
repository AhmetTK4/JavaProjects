package com.example.deneme;

import java.util.Random;

public class Grid {
    private Cell[][] cells;
    private int rows;
    private int cols;
    private Random random;

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        cells = new Cell[rows][cols];
        random = new Random();
        // Tüm hücreleri başlat
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new Cell();
            }
        }

        initializePattern();
    }

    private void initializePattern() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // %30 olasılıkla hücreyi canlı yap
                cells[i][j].setAlive(random.nextDouble() < 0.4);
            }
        }
    }


    // Her hücre için komşuları say
    public void countNeighbours() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int neighbours = 0;

                // Komşuları kontrol et
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        if (x == 0 && y == 0) continue; // Kendini kontrol etme

                        int nx = i + x;
                        int ny = j + y;

                        // Geçerli bir komşuysa
                        if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && cells[nx][ny].isAlive()) {
                            neighbours++;
                        }
                    }
                }
                cells[i][j].setNeighbours((short) neighbours);
            }
        }
    }

    // Hücreleri güncelle
    public void updateCells() {
        countNeighbours();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = cells[i][j];
                if (cell.isAlive()) {
                    if (cell.getNeighbours() < 2 || cell.getNeighbours() > 3) {
                        cell.setAlive(false); // Kalabalık veya yalnızlık yüzünden ölüyor
                    }
                } else {
                    if (cell.getNeighbours() == 3) {
                        cell.setAlive(true); // Doğum kuralı
                    }
                }
            }
        }
    }

    // Izgarayı konsolda göster
    public void displayGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(cells[i][j].isAlive() ? "O " : ". ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
