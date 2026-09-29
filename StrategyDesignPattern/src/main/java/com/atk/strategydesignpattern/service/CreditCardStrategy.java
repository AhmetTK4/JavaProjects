package com.atk.strategydesignpattern.service;

import com.atk.strategydesignpattern.entity.Order;
import com.atk.strategydesignpattern.entity.OrderStatus;
import com.atk.strategydesignpattern.entity.PaymentType;
import org.springframework.stereotype.Component;

@Component
public class CreditCardStrategy implements PaymentStrategy {

    @Override
    public PaymentType type() {
        return PaymentType.CREDIT_CARD;
    }

    @Override
    public void pay(Order order) {
        order.setStatus(OrderStatus.PAID_BY_CREDIT_CARD);
    }
}
