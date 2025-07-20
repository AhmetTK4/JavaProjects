package com.atk.strategydesignpattern.service;

import com.atk.strategydesignpattern.entity.Order;
import org.springframework.stereotype.Component;

@Component("CREDIT_CARD")
public class CreditCardStrategy implements PaymentStrategy {
    @Override
    public void pay(Order order) {
        order.setStatus("PAID_BY_CREDIT_CARD");
    }
}
