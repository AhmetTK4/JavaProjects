package com.example.gameoflife;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class GridController {

    static final int MAX_SIZE = 200;
    static final String SESSION_KEY = GridController.class.getName() + ".grid";
    private static final double INITIAL_DENSITY = 0.3;

    private final Random random = new Random();

    /**
     * Each browser session has its own board. The first call (or {@code reset=true}, or a size
     * change) returns a new random board; later calls return the next generation.
     */
    @GetMapping("/nextGeneration")
    public boolean[][] getNextGeneration(
            @RequestParam @Min(1) @Max(MAX_SIZE) int rows,
            @RequestParam @Min(1) @Max(MAX_SIZE) int cols,
            @RequestParam(defaultValue = "false") boolean reset,
            HttpSession session) {

        Grid current = (Grid) session.getAttribute(SESSION_KEY);
        Grid result;
        if (reset || current == null || current.rows() != rows || current.cols() != cols) {
            result = Grid.random(rows, cols, INITIAL_DENSITY, random);
        } else {
            result = current.next();
        }
        session.setAttribute(SESSION_KEY, result);
        return result.cells();
    }
}
