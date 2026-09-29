package com.atk.strategydesignpattern.exception;

public class OrderAlreadyPaidException extends RuntimeException {
    public OrderAlreadyPaidException(Long id) {
        super("Order " + id + " is already paid.");
    }
}
