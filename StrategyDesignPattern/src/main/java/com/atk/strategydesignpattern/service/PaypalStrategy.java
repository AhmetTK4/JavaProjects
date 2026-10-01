package com.atk.strategydesignpattern.service;

import com.atk.strategydesignpattern.entity.Order;
import com.atk.strategydesignpattern.entity.OrderStatus;
import com.atk.strategydesignpattern.entity.PaymentType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PaypalStrategy implements PaymentStrategy {

    private static final BigDecimal RATE = new BigDecimal("0.034");
    private static final BigDecimal FIXED = new BigDecimal("0.35");

    @Override
    public PaymentType type() {
        return PaymentType.PAYPAL;
    }

    @Override
    public void pay(Order order) {
        order.setStatus(OrderStatus.PAID_BY_PAYPAL);
    }

    /** 3.4% of the amount plus a fixed 0.35. */
    @Override
    public BigDecimal fee(BigDecimal amount) {
        return amount.multiply(RATE).add(FIXED).setScale(2, RoundingMode.HALF_UP);
    }
}
