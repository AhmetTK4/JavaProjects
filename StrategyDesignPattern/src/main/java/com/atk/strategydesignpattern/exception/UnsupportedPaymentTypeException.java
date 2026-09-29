package com.atk.strategydesignpattern.exception;

import com.atk.strategydesignpattern.entity.PaymentType;

public class UnsupportedPaymentTypeException extends RuntimeException {
    public UnsupportedPaymentTypeException(PaymentType paymentType) {
        super("Unsupported payment type: " + paymentType);
    }
}
