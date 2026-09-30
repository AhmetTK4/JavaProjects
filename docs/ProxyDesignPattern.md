# ProxyDesignPattern Service

Example project implementing the Proxy design pattern with REST endpoints.

`UserServiceProxy` is a *protection proxy*: it implements the same `UserService` interface as
`UserServiceImpl` (the real subject) and checks the **caller's** role before delegating write
operations. The controller depends only on `UserService`; the proxy is the `@Primary` bean.

Learning simplification: the caller is identified by the `X-User` request header
(`adminUser` or `regularUser` from the seed data). A real application would use the
authenticated principal (for example Spring Security) instead of a client-supplied header.

## Three kinds of proxies in this project

| Kind | Where | How |
|---|---|---|
| Hand-written (static) proxy | `UserServiceProxy` | A class implementing `UserService` that adds the admin check before delegating. |
| Caching proxy | `CachingUserServiceProxy` | Answers repeated reads from memory (TTL `app.user-cache.ttl`, default 1 min) and invalidates a user's entries when their email changes. Statistics: `GET /api/users/cache/stats`. |
| JDK dynamic proxy | `dynamic/DynamicProxies.timed(...)` | `java.lang.reflect.Proxy` generates the class at runtime; one `InvocationHandler` times every method of any interface. |
| Spring AOP proxy | `UserServiceImpl` (`@Transactional`) | Spring wraps the bean so transactions start and end around each call (`SpringProxyTest`). |

Because every proxy implements `UserService`, they can be chained without knowing about each other:

```
UserController -> UserServiceProxy (protection) -> CachingUserServiceProxy (caching) -> UserServiceImpl (real subject)
```

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
