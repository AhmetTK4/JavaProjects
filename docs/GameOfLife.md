# GameOfLife Service

Spring Boot implementation demonstrating Conway's Game of Life. Provides a REST endpoint to retrieve the next generation grid.

## Endpoints

| Method | Path              | Description                                |
|-------|------------------|--------------------------------------------|
| GET   | `/nextGeneration` | Returns the next generation of cells based on provided `rows` and `cols` query parameters. |

