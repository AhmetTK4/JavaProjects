package com.atk.proxydesignpattern.service;

import com.atk.proxydesignpattern.entity.User;
import com.atk.proxydesignpattern.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CachingUserServiceProxyTest {

    /** Clock that tests can move forward. */
    private static final class MutableClock extends Clock {
        private Instant now = Instant.parse("2026-01-01T00:00:00Z");

        void advance(Duration duration) {
            now = now.plus(duration);
        }

        @Override
        public ZoneId getZone() {
            return ZoneId.of("UTC");
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return now;
        }
    }

    private final UserService target = mock(UserService.class);
    private final MutableClock clock = new MutableClock();
    private final CachingUserServiceProxy proxy = new CachingUserServiceProxy(target, Duration.ofMinutes(1), clock);
    private User admin;

    @BeforeEach
    void setUp() {
        admin = new User("adminUser", "admin", "admin@example.com");
        admin.setId(1L);
        when(target.getUserById(1L)).thenReturn(Optional.of(admin));
        when(target.findByUsername("adminUser")).thenReturn(admin);
    }

    @Test
    void repeatedReadsHitTheRealSubjectOnce() {
        assertSame(admin, proxy.getUserById(1L).orElseThrow());
        assertSame(admin, proxy.getUserById(1L).orElseThrow());
        assertSame(admin, proxy.findByUsername("adminUser"));
        assertSame(admin, proxy.findByUsername("adminUser"));

        verify(target, times(1)).getUserById(1L);
        verify(target, times(1)).findByUsername("adminUser");
        assertEquals(new CachingUserServiceProxy.CacheStats(2, 2, 2), proxy.stats());
    }

    @Test
    void updateInvalidatesEntriesUnderEveryKey() {
        proxy.getUserById(1L);
        proxy.findByUsername("adminUser");

        proxy.updateUserEmail(1L, "new@example.com");
        proxy.getUserById(1L);
        proxy.findByUsername("adminUser");

        verify(target).updateUserEmail(1L, "new@example.com");
        verify(target, times(2)).getUserById(1L);
        verify(target, times(2)).findByUsername("adminUser");
    }

    @Test
    void failedUpdateKeepsCache() {
        doThrow(new UserNotFoundException("x")).when(target).updateUserEmail(1L, "new@example.com");
        proxy.getUserById(1L);
        assertThrows(UserNotFoundException.class, () -> proxy.updateUserEmail(1L, "new@example.com"));
        proxy.getUserById(1L);
        verify(target, times(1)).getUserById(1L);
    }

    @Test
    void entriesExpireAfterTtl() {
        proxy.getUserById(1L);
        clock.advance(Duration.ofSeconds(59));
        proxy.getUserById(1L);
        clock.advance(Duration.ofSeconds(1));
        proxy.getUserById(1L);
        verify(target, times(2)).getUserById(1L);
    }

    @Test
    void missesAreNotCached() {
        when(target.getUserById(2L)).thenReturn(Optional.empty());
        when(target.findByUsername("ghost")).thenThrow(new UserNotFoundException("ghost"));

        assertTrue(proxy.getUserById(2L).isEmpty());
        assertTrue(proxy.getUserById(2L).isEmpty());
        assertThrows(UserNotFoundException.class, () -> proxy.findByUsername("ghost"));
        assertThrows(UserNotFoundException.class, () -> proxy.findByUsername("ghost"));

        verify(target, times(2)).getUserById(2L);
        verify(target, times(2)).findByUsername("ghost");
        assertEquals(0, proxy.stats().entries());
    }
}
