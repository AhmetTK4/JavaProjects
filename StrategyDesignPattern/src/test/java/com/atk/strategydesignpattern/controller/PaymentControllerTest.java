package com.atk.strategydesignpattern.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void createPayAndRejectSecondPayment() throws Exception {
        String location = mvc.perform(post("/api/orders").param("type", "PAYPAL").param("amount", "19.99"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.amount").value(19.99))
                .andReturn().getResponse().getHeader("Location");

        mvc.perform(post(location + "/pay"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID_BY_PAYPAL"));
        mvc.perform(post(location + "/pay"))
                .andExpect(status().isConflict());
    }

    @Test
    void unknownPaymentTypeIsBadRequest() throws Exception {
        mvc.perform(post("/api/orders").param("type", "BITCOIN").param("amount", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void nonPositiveOrTooPreciseAmountIsBadRequest() throws Exception {
        mvc.perform(post("/api/orders").param("type", "PAYPAL").param("amount", "-5"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/orders").param("type", "PAYPAL").param("amount", "0"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/orders").param("type", "PAYPAL").param("amount", "1.001"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void missingOrderIsNotFound() throws Exception {
        mvc.perform(get("/api/orders/9999")).andExpect(status().isNotFound());
        mvc.perform(post("/api/orders/9999/pay"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail", containsString("9999")));
    }

    @Test
    void bankTransferReturnsPaymentReference() throws Exception {
        String location = mvc.perform(post("/api/orders").param("type", "BANK_TRANSFER").param("amount", "250"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentReference").doesNotExist())
                .andReturn().getResponse().getHeader("Location");

        mvc.perform(post(location + "/pay"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID_BY_BANK_TRANSFER"))
                .andExpect(jsonPath("$.paymentReference", startsWith("BT-")));
    }
}
