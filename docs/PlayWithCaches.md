# PlayWithCaches Service

Demonstrates caching with Spring's cache abstraction and Caffeine. `SlowDataSource` simulates an
expensive call (3 s by default); `DataService.getData` is `@Cacheable`, so only the first request
per parameter is slow until the entry expires.

## Endpoints

| Method | Path   | Parameters | Description                |
|-------|-------|------------|----------------------------|
| GET   | `/data` | `param` (query) | Retrieves data, using caching for repeated calls. |
| DELETE | `/data` | `param` (optional) | `@CacheEvict`: removes one entry, or all entries without `param`. |
| PUT   | `/data` | `param` (query), text body | `@CachePut` write-through: stores the value in the source and replaces the cache entry. |
| GET   | `/reference/{key}` | – | Reference data cached in `referenceCache`, which has its own TTL. |
| GET   | `/data/stats` | `cache` (optional, default `dataCache`) | Caffeine statistics of `dataCache` or `referenceCache`: size, hits, misses, hit rate, evictions. `404` for unknown caches. |

## Configuration

| Property | Default | Description |
|---|---|---|
| `app.data.simulated-delay` | `PT3S` | Latency of the simulated source |
| `app.cache.ttl` | `PT10S` | `expireAfterWrite` of cache entries |
| `app.cache.maximum-size` | `100` | Maximum number of cached entries |
| `app.cache.reference-ttl` | `PT1H` | `expireAfterWrite` of `referenceCache`, overriding the default spec |

```bash
time curl 'http://localhost:8080/data?param=a'   # ~3 s (miss)
time curl 'http://localhost:8080/data?param=a'   # fast (hit)
curl http://localhost:8080/data/stats
```
