package com.atk.strategydesignpattern.service;

import com.atk.strategydesignpattern.entity.Order;
import com.atk.strategydesignpattern.entity.PaymentType;

import java.math.BigDecimal;

public interface PaymentStrategy {

    /** The payment type this strategy handles; used to select it at runtime. */
    PaymentType type();

    void pay(Order order);

    /**
     * Fee charged for paying {@code amount} with this method, rounded to cents.
     * Each strategy brings its own algorithm; the default is a free payment method.
     */
    default BigDecimal fee(BigDecimal amount) {
        return BigDecimal.ZERO.setScale(2);
    }
}
