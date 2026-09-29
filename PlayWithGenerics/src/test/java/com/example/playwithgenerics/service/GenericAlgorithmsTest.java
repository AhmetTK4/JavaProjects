package com.example.playwithgenerics.service;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class GenericAlgorithmsTest {

    @Test
    void maxWorksForAnyComparableType() {
        assertEquals(9, GenericAlgorithms.max(List.of(3, 9, 1)));
        assertEquals("pear", GenericAlgorithms.max(List.of("apple", "pear", "fig")));
        assertThrows(NoSuchElementException.class, () -> GenericAlgorithms.max(List.<Integer>of()));
    }

    @Test
    void copyMovesProducerElementsIntoConsumerOfSupertype() {
        List<Integer> integers = List.of(1, 2);
        List<Number> numbers = new ArrayList<>(List.of(0.5));
        GenericAlgorithms.copy(integers, numbers);
        assertEquals(List.of(0.5, 1, 2), numbers);
    }

    @Test
    void sumAcceptsListsOfAnyNumberSubtype() {
        assertEquals(6.0, GenericAlgorithms.sum(List.of(1, 2, 3)));
        assertEquals(1.5, GenericAlgorithms.sum(List.of(0.5, 1.0)));
    }
}
