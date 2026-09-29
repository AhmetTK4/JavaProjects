package com.example.playwithgenerics.service;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Small static examples of bounded type parameters and wildcards.
 */
public final class GenericAlgorithms {

    private GenericAlgorithms() {
    }

    /**
     * Bounded type parameter: {@code T} must be comparable to itself or a supertype.
     * {@code ? super T} lets it work for types like {@code java.sql.Date} whose
     * {@code compareTo} is inherited from {@code java.util.Date}.
     */
    public static <T extends Comparable<? super T>> T max(Collection<? extends T> values) {
        if (values.isEmpty()) {
            throw new NoSuchElementException("Collection is empty");
        }
        T best = null;
        for (T value : values) {
            if (best == null || value.compareTo(best) > 0) {
                best = value;
            }
        }
        return best;
    }

    /**
     * PECS ("producer extends, consumer super"): {@code source} produces {@code T}s,
     * {@code target} consumes them. Copies e.g. {@code List<Integer>} into {@code List<Number>}.
     */
    public static <T> void copy(List<? extends T> source, List<? super T> target) {
        target.addAll(source);
    }

    /**
     * Upper-bounded wildcard: accepts {@code List<Integer>}, {@code List<Double>}, ...
     */
    public static double sum(Collection<? extends Number> numbers) {
        double total = 0;
        for (Number number : numbers) {
            total += number.doubleValue();
        }
        return total;
    }
}
