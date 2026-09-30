package com.atk.proxydesignpattern.dynamic;

import com.atk.proxydesignpattern.entity.User;
import com.atk.proxydesignpattern.exception.UserNotFoundException;
import com.atk.proxydesignpattern.service.UserService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DynamicProxiesTest {

    @Test
    void delegatesCallsAndReportsEachInvocation() {
        UserService target = mock(UserService.class);
        User user = new User("adminUser", "admin", "admin@example.com");
        when(target.getUserById(1L)).thenReturn(Optional.of(user));
        List<String> calls = new ArrayList<>();

        UserService proxy = DynamicProxies.timed(UserService.class, target,
                (method, duration) -> calls.add(method.getName()));

        assertSame(user, proxy.getUserById(1L).orElseThrow());
        proxy.updateUserEmail(1L, "new@example.com");
        verify(target).updateUserEmail(1L, "new@example.com");
        assertEquals(List.of("getUserById", "updateUserEmail"), calls);
        assertTrue(Proxy.isProxyClass(proxy.getClass()), "class is generated at runtime");
    }

    @Test
    void exceptionsFromTargetAreNotWrapped() {
        UserService target = mock(UserService.class);
        when(target.findByUsername("ghost")).thenThrow(new UserNotFoundException("ghost"));
        List<String> calls = new ArrayList<>();
        UserService proxy = DynamicProxies.timed(UserService.class, target, (m, d) -> calls.add(m.getName()));

        assertThrows(UserNotFoundException.class, () -> proxy.findByUsername("ghost"));
        assertEquals(List.of("findByUsername"), calls, "failed calls are measured too");
    }

    @Test
    void onlyInterfacesCanBeProxied() {
        assertThrows(IllegalArgumentException.class,
                () -> DynamicProxies.timed(String.class, "x", (m, d) -> { }));
    }
}
