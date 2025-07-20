package com.atk.strategydesignpattern;

import com.atk.strategydesignpattern.entity.Order;
import com.atk.strategydesignpattern.repository.OrderRepository;
import com.atk.strategydesignpattern.service.PaymentService;
import com.atk.strategydesignpattern.service.PaymentServiceImpl;
import com.atk.strategydesignpattern.service.CreditCardStrategy;
import com.atk.strategydesignpattern.service.PaypalStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


@DataJpaTest
@Import({PaymentServiceImpl.class, CreditCardStrategy.class, PaypalStrategy.class})
class PaymentServiceTest {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private PaymentService service;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void payOrderUsesCorrectStrategy() {
        Order order = service.createOrder("CREDIT_CARD", 50.0);
        service.payOrder(order.getId());
        Order paid = repository.findById(order.getId()).orElseThrow();
        Assertions.assertEquals("PAID_BY_CREDIT_CARD", paid.getStatus());
    }

    @Test
    void unknownPaymentTypeThrows() {
        Order order = repository.save(new Order("UNKNOWN", 10.0, "CREATED"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.payOrder(order.getId()));
    }
}
