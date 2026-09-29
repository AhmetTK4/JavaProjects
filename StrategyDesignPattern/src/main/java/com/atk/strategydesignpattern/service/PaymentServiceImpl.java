package com.atk.strategydesignpattern.service;

import com.atk.strategydesignpattern.entity.Order;
import com.atk.strategydesignpattern.entity.OrderStatus;
import com.atk.strategydesignpattern.entity.PaymentType;
import com.atk.strategydesignpattern.exception.OrderAlreadyPaidException;
import com.atk.strategydesignpattern.exception.OrderNotFoundException;
import com.atk.strategydesignpattern.exception.UnsupportedPaymentTypeException;
import com.atk.strategydesignpattern.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final Map<PaymentType, PaymentStrategy> strategies = new EnumMap<>(PaymentType.class);

    public PaymentServiceImpl(OrderRepository orderRepository, List<PaymentStrategy> strategies) {
        this.orderRepository = orderRepository;
        for (PaymentStrategy strategy : strategies) {
            PaymentStrategy previous = this.strategies.put(strategy.type(), strategy);
            if (previous != null) {
                throw new IllegalStateException("Multiple strategies registered for " + strategy.type());
            }
        }
    }

    @Override
    public Optional<Order> findOrder(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order createOrder(PaymentType paymentType, BigDecimal amount) {
        // Reject unsupported types when the order is created, not when it is paid.
        strategyFor(paymentType);
        return orderRepository.save(new Order(paymentType, amount, OrderStatus.CREATED));
    }

    @Override
    @Transactional
    public Order payOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        if (order.isPaid()) {
            throw new OrderAlreadyPaidException(id);
        }
        strategyFor(order.getPaymentType()).pay(order);
        return orderRepository.save(order);
    }

    private PaymentStrategy strategyFor(PaymentType paymentType) {
        PaymentStrategy strategy = strategies.get(paymentType);
        if (strategy == null) {
            throw new UnsupportedPaymentTypeException(paymentType);
        }
        return strategy;
    }
}
