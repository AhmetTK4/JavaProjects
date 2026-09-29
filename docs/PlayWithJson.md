# PlayWithJson Service

Demonstrates handling of JSON files via REST endpoints using Spring Boot and Jackson 3
(the `JsonMapper` auto-configured by Spring Boot 4).

Entries are stored in the file configured by `app.data-file` (environment variable `DATA_FILE`,
default `data/entries.json` relative to the working directory). On first start the file is created
from `classpath:seed-data.json`. Writes are serialised with a read/write lock and replace the file
atomically, so concurrent requests do not lose updates.

## Endpoints

| Method | Path | Description |
|-------|------|-------------|
| GET | `/api/data` | Retrieve all entries. |
| POST | `/api/data` | Add an entry from `{"name": "..."}` (1–100 characters). Returns `201 Created` with a `Location` header; blank names return `400`. |
| DELETE | `/api/data/{id}` | Delete an entry. Returns `204`, or `404` if it does not exist. |
