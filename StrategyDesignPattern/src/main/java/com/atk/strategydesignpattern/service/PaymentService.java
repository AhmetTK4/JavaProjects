package com.atk.strategydesignpattern.service;

import com.atk.strategydesignpattern.entity.Order;
import com.atk.strategydesignpattern.entity.PaymentType;

import java.math.BigDecimal;
import java.util.Optional;

public interface PaymentService {
    Optional<Order> findOrder(Long id);
    Order createOrder(PaymentType paymentType, BigDecimal amount);
    Order payOrder(Long id);
}
