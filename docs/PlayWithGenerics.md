# PlayWithGenerics Service

Spring Boot project demonstrating usage of generics with REST APIs.

- `GenericService<T>` is a thread-safe in-memory store. `GenericServiceConfig` registers one
  instance per element type, and Spring injects `GenericService<User>` or `GenericService<String>`
  by matching the generic type.
- `GenericService.max(Comparator<? super T>)` and `map(Function<? super T, ? extends R>)` show
  wildcards and a generic method with its own type parameter.
- `GenericAlgorithms` shows a bounded type parameter (`<T extends Comparable<? super T>>`) and
  PECS (`copy(List<? extends T>, List<? super T>)`, `sum(Collection<? extends Number>)`).

## Endpoints

| Method | Path | Description |
|-------|------|-------------|
| POST | `/api/generic/user` | Add a `User` (`name` not blank, `age` 0–150). |
| GET | `/api/generic/users` | Retrieve all stored `User` objects. |
| GET | `/api/generic/users/oldest` | The oldest user, or `404` when there are none. |
| GET | `/api/generic/users/names` | Names of all users. |
| POST | `/api/generic/string` | Add a new `String` value to the list. |
| GET | `/api/generic/strings` | Retrieve all stored strings. |
