package com.atk.strategydesignpattern.service;

import com.atk.strategydesignpattern.entity.Order;

public interface PaymentStrategy {
    void pay(Order order);
}
