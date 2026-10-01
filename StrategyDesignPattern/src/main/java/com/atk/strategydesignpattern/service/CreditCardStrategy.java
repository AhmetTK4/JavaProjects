package com.atk.strategydesignpattern.service;

import com.atk.strategydesignpattern.entity.Order;
import com.atk.strategydesignpattern.entity.OrderStatus;
import com.atk.strategydesignpattern.entity.PaymentType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CreditCardStrategy implements PaymentStrategy {

    private static final BigDecimal RATE = new BigDecimal("0.025");

    @Override
    public PaymentType type() {
        return PaymentType.CREDIT_CARD;
    }

    @Override
    public void pay(Order order) {
        order.setStatus(OrderStatus.PAID_BY_CREDIT_CARD);
    }

    /** 2.5% of the amount. */
    @Override
    public BigDecimal fee(BigDecimal amount) {
        return amount.multiply(RATE).setScale(2, RoundingMode.HALF_UP);
    }
}
