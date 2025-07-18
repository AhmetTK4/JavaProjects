package com.example.deneme;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameRulesTest {

    @Test
    void birthRuleSetsAliveWhenExactlyThreeNeighbors() {
        Cell cell = new Cell();
        cell.setAlive(false);
        cell.setNeighbours(3);
        Game game = new Game(1, 1);
        game.applyBirthRule(cell);
        assertTrue(cell.isAlive(), "Cell should become alive when it has exactly three neighbours");
    }

    @Test
    void birthRuleKeepsDeadWhenNotThreeNeighbors() {
        Cell cell = new Cell();
        cell.setAlive(false);
        cell.setNeighbours(2);
        Game game = new Game(1, 1);
        game.applyBirthRule(cell);
        assertFalse(cell.isAlive(), "Cell should remain dead when it does not have three neighbours");
    }

    @Test
    void survivalRuleKeepsAliveWithTwoOrThreeNeighbors() {
        Cell cell = new Cell();
        cell.setNeighbours(2);
        Game game = new Game(1, 1);
        game.applySurvivalRule(cell);
        assertTrue(cell.isAlive(), "Cell should stay alive with two neighbours");

        cell.setNeighbours(3);
        game.applySurvivalRule(cell);
        assertTrue(cell.isAlive(), "Cell should stay alive with three neighbours");
    }

    @Test
    void survivalRuleKillsWithOtherNeighborCounts() {
        Cell cell = new Cell();
        cell.setNeighbours(1);
        Game game = new Game(1, 1);
        game.applySurvivalRule(cell);
        assertFalse(cell.isAlive(), "Cell should die with neighbour count other than two or three");
    }
}
