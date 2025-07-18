# PlayWithJson Service

Demonstrates handling of JSON files via REST endpoints using Spring Boot.

## Endpoints

| Method | Path | Description |
|-------|------|-------------|
| GET | `/api/data` | Retrieve all entries from `data.json`. |
| POST | `/api/data` | Add a new entry and persist to `data.json`. |
| DELETE | `/api/data/{id}` | Delete an entry by its ID. |

