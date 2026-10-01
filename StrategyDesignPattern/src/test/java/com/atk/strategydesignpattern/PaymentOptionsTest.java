package com.atk.strategydesignpattern;

import com.atk.strategydesignpattern.dto.PaymentOption;
import com.atk.strategydesignpattern.entity.Order;
import com.atk.strategydesignpattern.entity.PaymentType;
import com.atk.strategydesignpattern.repository.OrderRepository;
import com.atk.strategydesignpattern.service.CreditCardStrategy;
import com.atk.strategydesignpattern.service.PaymentServiceImpl;
import com.atk.strategydesignpattern.service.PaymentStrategy;
import com.atk.strategydesignpattern.service.PaypalStrategy;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentOptionsTest {

    @Autowired
    MockMvc mvc;

    @Test
    void eachStrategyHasItsOwnFeeAlgorithm() {
        assertEquals(new BigDecimal("2.50"), new CreditCardStrategy().fee(new BigDecimal("100")));
        assertEquals(new BigDecimal("3.75"), new PaypalStrategy().fee(new BigDecimal("100")));
        // Rounded half-up to cents: 19.99 * 2.5% = 0.49975, 19.99 * 3.4% + 0.35 = 1.02966
        assertEquals(new BigDecimal("0.50"), new CreditCardStrategy().fee(new BigDecimal("19.99")));
        assertEquals(new BigDecimal("1.03"), new PaypalStrategy().fee(new BigDecimal("19.99")));
    }

    @Test
    void strategiesWithoutAFeeAlgorithmAreFree() {
        PaymentStrategy free = new PaymentStrategy() {
            @Override
            public PaymentType type() {
                return PaymentType.PAYPAL;
            }

            @Override
            public void pay(Order order) {
            }
        };
        assertEquals(new BigDecimal("0.00"), free.fee(new BigDecimal("100")));
    }

    @Test
    void optionsAreSortedCheapestFirst() {
        PaymentServiceImpl service = new PaymentServiceImpl(mock(OrderRepository.class),
                List.of(new PaypalStrategy(), new CreditCardStrategy()));

        List<PaymentOption> options = service.paymentOptions(new BigDecimal("100.00"));

        assertEquals(List.of(
                new PaymentOption(PaymentType.CREDIT_CARD, new BigDecimal("2.50"), new BigDecimal("102.50")),
                new PaymentOption(PaymentType.PAYPAL, new BigDecimal("3.75"), new BigDecimal("103.75"))), options);
    }

    @Test
    void endpointListsEveryMethodCheapestFirst() throws Exception {
        String json = mvc.perform(get("/api/orders/payment-options").param("amount", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.type == 'CREDIT_CARD')].fee").value(2.50))
                .andExpect(jsonPath("$[?(@.type == 'PAYPAL')].total").value(103.75))
                .andReturn().getResponse().getContentAsString();

        List<Double> totals = JsonPath.read(json, "$[*].total");
        assertEquals(totals.stream().sorted().toList(), totals, "options must be sorted by total");
        assertEquals(PaymentType.values().length, totals.size(), "one option per payment type");
    }

    @Test
    void invalidAmountIsBadRequest() throws Exception {
        mvc.perform(get("/api/orders/payment-options").param("amount", "0")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/orders/payment-options").param("amount", "1.001")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/orders/payment-options")).andExpect(status().isBadRequest());
    }
}
