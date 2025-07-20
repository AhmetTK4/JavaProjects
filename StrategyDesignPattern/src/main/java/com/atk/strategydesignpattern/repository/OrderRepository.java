package com.atk.strategydesignpattern.repository;

import com.atk.strategydesignpattern.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
