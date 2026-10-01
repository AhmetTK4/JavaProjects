package com.atk.strategydesignpattern;

import com.atk.strategydesignpattern.entity.Order;
import com.atk.strategydesignpattern.entity.OrderStatus;
import com.atk.strategydesignpattern.entity.PaymentType;
import com.atk.strategydesignpattern.exception.OrderAlreadyPaidException;
import com.atk.strategydesignpattern.exception.OrderNotFoundException;
import com.atk.strategydesignpattern.exception.UnsupportedPaymentTypeException;
import com.atk.strategydesignpattern.repository.OrderRepository;
import com.atk.strategydesignpattern.service.BankTransferStrategy;
import com.atk.strategydesignpattern.service.CreditCardStrategy;
import com.atk.strategydesignpattern.service.PaymentService;
import com.atk.strategydesignpattern.service.PaymentServiceImpl;
import com.atk.strategydesignpattern.service.PaypalStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PaymentServiceTest {

    @Autowired
    private OrderRepository repository;

    private PaymentService service;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        service = new PaymentServiceImpl(repository, List.of(new CreditCardStrategy(), new PaypalStrategy(), new BankTransferStrategy()));
    }

    @Test
    void payOrderUsesCreditCardStrategy() {
        Order order = service.createOrder(PaymentType.CREDIT_CARD, new BigDecimal("50.00"));
        service.payOrder(order.getId());
        assertEquals(OrderStatus.PAID_BY_CREDIT_CARD, repository.findById(order.getId()).orElseThrow().getStatus());
    }

    @Test
    void payOrderUsesPaypalStrategy() {
        Order order = service.createOrder(PaymentType.PAYPAL, new BigDecimal("50.00"));
        service.payOrder(order.getId());
        assertEquals(OrderStatus.PAID_BY_PAYPAL, repository.findById(order.getId()).orElseThrow().getStatus());
    }

    @Test
    void orderWithoutStrategyIsRejectedAtCreation() {
        PaymentService creditCardOnly = new PaymentServiceImpl(repository, List.of(new CreditCardStrategy()));
        assertThrows(UnsupportedPaymentTypeException.class,
                () -> creditCardOnly.createOrder(PaymentType.PAYPAL, BigDecimal.TEN));
        assertEquals(0, repository.count());
    }

    @Test
    void paidOrderCannotBePaidAgain() {
        Order order = service.createOrder(PaymentType.CREDIT_CARD, BigDecimal.TEN);
        service.payOrder(order.getId());
        assertThrows(OrderAlreadyPaidException.class, () -> service.payOrder(order.getId()));
    }

    @Test
    void payingMissingOrderThrowsNotFound() {
        assertThrows(OrderNotFoundException.class, () -> service.payOrder(9999L));
    }

    @Test
    void duplicateStrategiesAreRejected() {
        assertThrows(IllegalStateException.class,
                () -> new PaymentServiceImpl(repository, List.of(new CreditCardStrategy(), new CreditCardStrategy())));
    }

    @Test
    void bankTransferSetsStatusAndReference() {
        Order order = service.createOrder(PaymentType.BANK_TRANSFER, new BigDecimal("250.00"));
        service.payOrder(order.getId());
        Order paid = repository.findById(order.getId()).orElseThrow();
        assertEquals(OrderStatus.PAID_BY_BANK_TRANSFER, paid.getStatus());
        assertTrue(paid.getPaymentReference().matches("BT-" + order.getId() + "-[0-9A-F]{8}"),
                paid.getPaymentReference());
    }

    @Test
    void otherMethodsDoNotSetAReference() {
        Order order = service.createOrder(PaymentType.CREDIT_CARD, BigDecimal.TEN);
        service.payOrder(order.getId());
        assertNull(repository.findById(order.getId()).orElseThrow().getPaymentReference());
    }
}
