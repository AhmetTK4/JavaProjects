package com.example.deneme;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GridController {
    private GridBoot grid;

    @GetMapping("/nextGeneration")
    public CellBoot[][] getNextGeneration(
            @RequestParam int rows,
            @RequestParam int cols) {

        // Eğer grid daha önce oluşturulmadıysa veya boyutları değiştiyse yeni bir GridBoot oluştur
        if (grid == null || grid.getRows() != rows || grid.getCols() != cols) {
            grid = new GridBoot(rows, cols);
        }

        // Her nesil için hücreleri güncelle
        grid.updateCells();
        return grid.getCells();
    }
}
