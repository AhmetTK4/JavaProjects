# PlayWithCaches Service

Demonstrates caching with Spring's cache abstraction and Caffeine. `SlowDataSource` simulates an
expensive call (3 s by default); `DataService.getData` is `@Cacheable`, so only the first request
per parameter is slow until the entry expires.

`getData` uses `sync = true`: if many requests miss the same key at once, only one loads it and the
others wait for its result (cache stampede protection). Keys longer than 64 characters bypass the
cache (`condition`), so long arbitrary inputs cannot evict useful entries.

## Endpoints

| Method | Path   | Parameters | Description                |
|-------|-------|------------|----------------------------|
| GET   | `/data` | `param` (query) | Retrieves data, using caching for repeated calls. |
| DELETE | `/data` | `param` (optional) | `@CacheEvict`: removes one entry, or all entries without `param`. |
| GET   | `/data/stats` | – | Caffeine statistics: size, hits, misses, hit rate, evictions. |

## Configuration

| Property | Default | Description |
|---|---|---|
| `app.data.simulated-delay` | `PT3S` | Latency of the simulated source |
| `app.cache.ttl` | `PT10S` | `expireAfterWrite` of cache entries |
| `app.cache.maximum-size` | `100` | Maximum number of cached entries |

```bash
time curl 'http://localhost:8080/data?param=a'   # ~3 s (miss)
time curl 'http://localhost:8080/data?param=a'   # fast (hit)
curl http://localhost:8080/data/stats
```
