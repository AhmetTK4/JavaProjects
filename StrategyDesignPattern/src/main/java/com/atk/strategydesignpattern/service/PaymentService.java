package com.atk.strategydesignpattern.service;

import com.atk.strategydesignpattern.entity.Order;
import java.util.Optional;

public interface PaymentService {
    Optional<Order> findOrder(Long id);
    Order createOrder(String paymentType, double amount);
    Order payOrder(Long id);
}
