package com.atk.strategydesignpattern.controller;

import com.atk.strategydesignpattern.entity.Order;
import com.atk.strategydesignpattern.entity.PaymentType;
import com.atk.strategydesignpattern.exception.OrderNotFoundException;
import com.atk.strategydesignpattern.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/orders")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "Create order")
    @PostMapping
    public ResponseEntity<Order> create(@RequestParam PaymentType type,
                                        @RequestParam @DecimalMin("0.01") @Digits(integer = 17, fraction = 2) BigDecimal amount) {
        Order order = paymentService.createOrder(type, amount);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(order.getId()))
                .body(order);
    }

    @Operation(summary = "Pay order with the strategy for its payment type")
    @PostMapping("/{id}/pay")
    public Order pay(@PathVariable Long id) {
        return paymentService.payOrder(id);
    }

    @Operation(summary = "Get order")
    @GetMapping("/{id}")
    public Order get(@PathVariable Long id) {
        return paymentService.findOrder(id).orElseThrow(() -> new OrderNotFoundException(id));
    }
}
