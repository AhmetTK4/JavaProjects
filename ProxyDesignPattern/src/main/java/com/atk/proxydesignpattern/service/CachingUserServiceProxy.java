package com.atk.proxydesignpattern.service;

import com.atk.proxydesignpattern.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * Caching proxy: same {@link UserService} interface, but answers repeated reads from memory
 * instead of the database. Writes go to the real subject and invalidate the affected entries.
 * <p>
 * Proxies can be chained because they share one interface:
 * {@code UserServiceProxy (protection) -> CachingUserServiceProxy (caching) -> UserServiceImpl (real)}.
 * Neither proxy knows about the other.
 */
@Service
public class CachingUserServiceProxy implements UserService {

    private record Entry(User user, Instant expiresAt) {
    }

    private final UserService target;
    private final Duration ttl;
    private final Clock clock;
    private final Map<Long, Entry> byId = new ConcurrentHashMap<>();
    private final Map<String, Entry> byUsername = new ConcurrentHashMap<>();
    private final AtomicLong hits = new AtomicLong();
    private final AtomicLong misses = new AtomicLong();

    @Autowired
    public CachingUserServiceProxy(@Qualifier("userServiceImpl") UserService target,
                                   @Value("${app.user-cache.ttl:PT1M}") Duration ttl) {
        this(target, ttl, Clock.systemUTC());
    }

    CachingUserServiceProxy(UserService target, Duration ttl, Clock clock) {
        this.target = target;
        this.ttl = ttl;
        this.clock = clock;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        // Misses (unknown ids) are not cached, so a user created later is found immediately.
        return Optional.ofNullable(cached(byId, id, () -> target.getUserById(id).orElse(null)));
    }

    @Override
    public User findByUsername(String username) {
        // The real subject throws UserNotFoundException for unknown names; nothing is cached then.
        return cached(byUsername, username, () -> target.findByUsername(username));
    }

    @Override
    public void updateUserEmail(Long id, String newEmail) {
        target.updateUserEmail(id, newEmail);
        // A read running concurrently with this update may still cache the old value;
        // the TTL bounds how long such a stale entry can live.
        invalidate(id);
    }

    /** Removes every cached entry of this user, whichever key it was cached under. */
    public void invalidate(Long id) {
        byId.remove(id);
        byUsername.values().removeIf(entry -> id.equals(entry.user().getId()));
    }

    public CacheStats stats() {
        return new CacheStats(hits.get(), misses.get(), byId.size() + byUsername.size());
    }

    public record CacheStats(long hits, long misses, int entries) {
    }

    private <K> User cached(Map<K, Entry> cache, K key, Supplier<User> loader) {
        Instant now = clock.instant();
        Entry entry = cache.get(key);
        if (entry != null && now.isBefore(entry.expiresAt())) {
            hits.incrementAndGet();
            return entry.user();
        }
        misses.incrementAndGet();
        User user = loader.get();
        if (user != null) {
            cache.put(key, new Entry(user, now.plus(ttl)));
        } else {
            cache.remove(key);
        }
        return user;
    }
}
