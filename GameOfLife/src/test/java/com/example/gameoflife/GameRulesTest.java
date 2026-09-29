package com.example.gameoflife;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameRulesTest {

    @ParameterizedTest(name = "alive={0}, neighbours={1} -> {2}")
    @CsvSource({
            // Live cells: under-population, survival, over-population
            "true, 0, false", "true, 1, false",
            "true, 2, true", "true, 3, true",
            "true, 4, false", "true, 8, false",
            // Dead cells: birth only with exactly three neighbours
            "false, 0, false", "false, 2, false",
            "false, 3, true",
            "false, 4, false", "false, 8, false"
    })
    void appliesConwayRules(boolean alive, int neighbours, boolean expected) {
        assertEquals(expected, GameRules.nextState(alive, neighbours));
    }
}
