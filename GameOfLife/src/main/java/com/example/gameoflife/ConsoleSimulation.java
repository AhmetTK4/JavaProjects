package com.example.gameoflife;

import java.util.Random;

/**
 * Runs the simulation in the terminal without starting the web application.
 */
public class ConsoleSimulation {

    public static void main(String[] args) {
        int size = 20;
        int generations = 10;
        Grid grid = Grid.random(size, size, 0.3, new Random());
        for (int i = 1; i <= generations; i++) {
            System.out.println("Generation " + i);
            System.out.println(grid);
            grid = grid.next();
        }
    }
}
