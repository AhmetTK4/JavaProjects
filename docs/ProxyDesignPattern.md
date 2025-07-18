# ProxyDesignPattern Service

Example project implementing the Proxy design pattern with REST endpoints.

## Endpoints

| Method | Path | Description |
|-------|------|-------------|
| GET | `/api/users/{id}` | Get a user by id. |
| GET | `/api/users/name/{name}` | Find user by username. |
| PUT | `/api/users/{id}/email` | Update user email using `newEmail` parameter. |

