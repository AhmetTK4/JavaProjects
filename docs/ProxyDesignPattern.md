# ProxyDesignPattern Service

Example project implementing the Proxy design pattern with REST endpoints.

`UserServiceProxy` is a *protection proxy*: it implements the same `UserService` interface as
`UserServiceImpl` (the real subject) and checks the **caller's** role before delegating write
operations. The controller depends only on `UserService`; the proxy is the `@Primary` bean.

Learning simplification: the caller is identified by the `X-User` request header
(`adminUser` or `regularUser` from the seed data). A real application would use the
authenticated principal (for example Spring Security) instead of a client-supplied header.

## Endpoints

| Method | Path | Description |
|-------|------|-------------|
| GET | `/api/users/{id}` | Get a user by id. `404` if it does not exist. |
| GET | `/api/users/name/{name}` | Find user by username. `404` if it does not exist. |
| PUT | `/api/users/{id}/email` | Update user email using the `newEmail` parameter. Requires the `X-User` header of an admin. |

Errors are returned as [RFC 9457](https://www.rfc-editor.org/rfc/rfc9457) problem details:
`400` invalid email, `401` missing or unknown caller, `403` caller is not an admin, `404` user not found.

```bash
curl -X PUT -H 'X-User: adminUser' 'http://localhost:8080/api/users/2/email?newEmail=new@example.com'
```
