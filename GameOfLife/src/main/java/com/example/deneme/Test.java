package com.example.deneme;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
   /*     Cell cell = new Cell();
        cell.setAlive(false);
        cell.setNeighbours(3);
        Game game = new Game();
        game.applyBirthRule(cell);
        System.out.println("1 - Canli mi: "+ (cell.isAlive() ? "Yes": "No"));
        cell.setAlive(true);
        cell.setNeighbours(1);
        game.applySurvivalRule(cell);
        System.out.println("2 - üldü mü "+ (!cell.isAlive() ? "Yes": "No"));
        */
        Game game = new Game(40, 40); // 10x10 ızgara
        game.runSimulation(100); // 5 nesil simülasyonu çalıştır
    }
}
