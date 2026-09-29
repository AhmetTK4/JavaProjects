package com.example.playwithgenerics.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

/**
 * In-memory store for any element type {@code T}.
 * Instances are registered per type in {@code GenericServiceConfig}; Spring injects the right one
 * by matching the generic type ({@code GenericService<User>} vs {@code GenericService<String>}).
 */
public class GenericService<T> {

    // Thread-safe: controllers are called concurrently.
    private final List<T> items = new CopyOnWriteArrayList<>();

    public void addItem(T item) {
        items.add(item);
    }

    /** Returns an unmodifiable snapshot so callers cannot change the internal list. */
    public List<T> getItems() {
        return List.copyOf(items);
    }

    /**
     * {@code Comparator<? super T>} (consumer side of PECS): a comparator written for a supertype of
     * {@code T} also works, e.g. a {@code Comparator<Object>} for {@code GenericService<String>}.
     */
    public Optional<T> max(Comparator<? super T> comparator) {
        return items.stream().max(comparator);
    }

    /**
     * Generic method with its own type parameter {@code R}: maps each item to another type.
     */
    public <R> List<R> map(Function<? super T, ? extends R> mapper) {
        return items.stream().<R>map(mapper).toList();
    }
}
