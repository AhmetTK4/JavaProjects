package com.atk.strategydesignpattern.dto;

import com.atk.strategydesignpattern.entity.PaymentType;

import java.math.BigDecimal;

public record PaymentOption(PaymentType type, BigDecimal fee, BigDecimal total) {
}
