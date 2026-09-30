package com.atk.proxydesignpattern.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.function.BiConsumer;

/**
 * Dynamic proxies with {@link java.lang.reflect.Proxy}: instead of writing one proxy class per
 * interface (like {@code UserServiceProxy}), the JDK generates the class at runtime and routes every
 * call through a single {@link InvocationHandler}. Spring AOP uses the same idea for
 * {@code @Transactional}, {@code @Cacheable} and similar annotations.
 */
public final class DynamicProxies {

    private DynamicProxies() {
    }

    /**
     * Wraps {@code target} so that the duration of every interface method call is reported to
     * {@code listener}. Works for any interface without writing a proxy class.
     */
    @SuppressWarnings("unchecked")
    public static <T> T timed(Class<T> iface, T target, BiConsumer<Method, Duration> listener) {
        if (!iface.isInterface()) {
            throw new IllegalArgumentException(iface.getName() + " is not an interface");
        }
        InvocationHandler handler = (proxy, method, args) -> {
            long start = System.nanoTime();
            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException e) {
                // Rethrow what the target threw, not the reflection wrapper.
                throw e.getCause();
            } finally {
                listener.accept(method, Duration.ofNanos(System.nanoTime() - start));
            }
        };
        return (T) Proxy.newProxyInstance(iface.getClassLoader(), new Class<?>[]{iface}, handler);
    }
}
