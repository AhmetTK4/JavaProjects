# kullanici-servis

Quarkus based service for managing users (`isim`, `email`) with Hibernate ORM Panache,
Flyway migrations and PostgreSQL.

## Endpoints

| Method | Path | Description |
|-------|------|-------------|
| GET | `/kullanici` | List all users. |
| GET | `/kullanici/{id}` | Get one user, `404` if missing. |
| POST | `/kullanici` | Create a user. Returns `201 Created` with `Location`; the id is always generated. |
| PUT | `/kullanici/{id}` | Update a user, `404` if missing. |
| DELETE | `/kullanici/{id}` | Delete a user. Returns `204`, or `404` if missing. |
| GET | `/q/health` | SmallRye Health (liveness and readiness). |
| GET | `/q/openapi`, `/q/swagger-ui` | OpenAPI document and UI (Swagger UI in dev mode). |

Validation errors (blank `isim`, invalid `email`) return `400`; an email that is already used returns `409`.
