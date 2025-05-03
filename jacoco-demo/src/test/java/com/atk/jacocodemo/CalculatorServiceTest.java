package com.atk.jacocodemo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalculatorServiceTest {

    private CalculatorService service;

    @BeforeEach
    void setUp() {
        service = new CalculatorService();
    }

    @Test
    void testAdd() {
        Assertions.assertEquals(5, service.add(2, 3));
    }

    @Test
    void testDivide() {
        Assertions.assertEquals(2, service.divide(4, 2));
    }

    @Test
    void testDivideByZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.divide(5, 0));
    }

    @Test
    void testAddNegative() {
        Assertions.assertEquals(-1, service.add(2, -3));
    }
}