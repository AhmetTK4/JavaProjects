package com.atk.strategydesignpattern.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    private String paymentType;
    private double amount;
    private String status;

    public Order(String paymentType, double amount, String status) {
        this.paymentType = paymentType;
        this.amount = amount;
        this.status = status;
    }
}
