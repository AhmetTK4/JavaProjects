package com.atk.strategydesignpattern.service;

import com.atk.strategydesignpattern.entity.Order;
import com.atk.strategydesignpattern.entity.PaymentType;

public interface PaymentStrategy {

    /** The payment type this strategy handles; used to select it at runtime. */
    PaymentType type();

    void pay(Order order);
}
