package com.atk.strategydesignpattern;

import com.atk.strategydesignpattern.entity.PaymentType;
import com.atk.strategydesignpattern.service.PaymentStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Guards the open/closed extension point: a new PaymentType without a strategy fails here, not in production. */
@SpringBootTest
class StrategyRegistrationTest {

    @Autowired
    List<PaymentStrategy> strategies;

    @Test
    void everyPaymentTypeHasExactlyOneStrategy() {
        Set<PaymentType> covered = strategies.stream().map(PaymentStrategy::type)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(PaymentType.class)));
        assertEquals(EnumSet.allOf(PaymentType.class), covered);
        assertEquals(PaymentType.values().length, strategies.size());
    }
}
