package com.atk.strategydesignpattern.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Order " + id + " not found.");
    }
}
