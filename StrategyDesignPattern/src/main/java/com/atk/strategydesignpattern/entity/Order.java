package com.atk.strategydesignpattern.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    // BigDecimal avoids binary floating-point rounding errors for money.
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    /** Reference the customer quotes on the transfer; set only by payment methods that need one. */
    @Column(length = 40)
    private String paymentReference;

    public Order(PaymentType paymentType, BigDecimal amount, OrderStatus status) {
        this.paymentType = paymentType;
        this.amount = amount;
        this.status = status;
    }

    public boolean isPaid() {
        return status != OrderStatus.CREATED;
    }
}
