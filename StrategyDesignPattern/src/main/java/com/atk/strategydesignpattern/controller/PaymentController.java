package com.atk.strategydesignpattern.controller;

import com.atk.strategydesignpattern.entity.Order;
import com.atk.strategydesignpattern.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "Create order")
    @PostMapping
    public ResponseEntity<Order> create(@RequestParam String type, @RequestParam double amount) {
        return ResponseEntity.ok(paymentService.createOrder(type, amount));
    }

    @Operation(summary = "Pay order")
    @PostMapping("/{id}/pay")
    public ResponseEntity<Order> pay(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.payOrder(id));
    }

    @Operation(summary = "Get order")
    @GetMapping("/{id}")
    public ResponseEntity<Order> get(@PathVariable Long id) {
        return paymentService.findOrder(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
