package com.atk.strategydesignpattern.service;

import com.atk.strategydesignpattern.entity.Order;
import com.atk.strategydesignpattern.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final Map<String, PaymentStrategy> strategies;

    public PaymentServiceImpl(OrderRepository orderRepository, Map<String, PaymentStrategy> strategies) {
        this.orderRepository = orderRepository;
        this.strategies = strategies;
    }

    @Override
    public Optional<Order> findOrder(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order createOrder(String paymentType, double amount) {
        Order order = new Order(paymentType, amount, "CREATED");
        return orderRepository.save(order);
    }

    @Override
    public Order payOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        PaymentStrategy strategy = strategies.get(order.getPaymentType());
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown payment type" + order.getPaymentType());
        }
        strategy.pay(order);
        return orderRepository.save(order);
    }
}
