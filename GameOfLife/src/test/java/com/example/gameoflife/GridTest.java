package com.example.gameoflife;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    private static Grid parse(String... rows) {
        boolean[][] cells = new boolean[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            cells[i] = new boolean[rows[i].length()];
            for (int j = 0; j < rows[i].length(); j++) {
                cells[i][j] = rows[i].charAt(j) == 'O';
            }
        }
        return new Grid(cells);
    }

    @Test
    void blinkerOscillatesWithPeriodTwo() {
        Grid horizontal = parse(".....", ".....", ".OOO.", ".....", ".....");
        Grid vertical = parse(".....", "..O..", "..O..", "..O..", ".....");
        assertEquals(vertical.toString(), horizontal.next().toString());
        assertEquals(horizontal.toString(), horizontal.next().next().toString());
    }

    @Test
    void blockIsStillLife() {
        Grid block = parse("....", ".OO.", ".OO.", "....");
        assertEquals(block.toString(), block.next().toString());
    }

    @Test
    void edgesDoNotWrapAround() {
        Grid grid = parse("O..", "...", "..O");
        assertEquals(0, grid.countAliveNeighbours(0, 2));
        assertEquals(2, grid.countAliveNeighbours(1, 1));
    }

    @Test
    void cellsReturnsDefensiveCopy() {
        Grid grid = parse("O");
        grid.cells()[0][0] = false;
        assertTrue(grid.cells()[0][0]);
    }

    @Test
    void randomGridHasRequestedSize() {
        Grid grid = Grid.random(3, 7, 0.5, new Random(42));
        assertEquals(3, grid.rows());
        assertEquals(7, grid.cols());
    }

    @Test
    void rejectsRaggedRows() {
        assertThrows(IllegalArgumentException.class, () -> new Grid(new boolean[][]{{true}, {true, false}}));
    }
}
