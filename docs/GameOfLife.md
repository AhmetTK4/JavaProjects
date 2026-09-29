# GameOfLife Service

Spring Boot implementation of Conway's Game of Life. `GameRules.nextState` holds the rules,
`Grid` is an immutable, non-wrapping board, and the web page at `/` animates it.

## Endpoints

| Method | Path              | Description                                |
|-------|------------------|--------------------------------------------|
| GET   | `/nextGeneration` | Returns the board as a `boolean[][]` (`true` = alive). Query parameters: `rows` and `cols` (1–200), optional `reset`. |

Each HTTP session has its own board. The first call, a call with `reset=true`, or a size change
returns a new random board; subsequent calls return the next generation.
Out-of-range sizes return `400` problem details.

To run the simulation in the terminal instead, run `com.example.gameoflife.ConsoleSimulation`.
