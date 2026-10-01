package com.atk.strategydesignpattern.service;

import com.atk.strategydesignpattern.entity.Order;
import com.atk.strategydesignpattern.entity.OrderStatus;
import com.atk.strategydesignpattern.entity.PaymentType;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;

/**
 * Bank transfer (havale/EFT). Adding it needed only this class and a {@link PaymentType} constant:
 * {@code PaymentServiceImpl} discovers every strategy bean and did not change (open/closed principle).
 * <p>
 * Unlike card or PayPal payments, the customer must quote a reference on the transfer so the
 * payment can be matched to the order.
 */
@Component
public class BankTransferStrategy implements PaymentStrategy {

    @Override
    public PaymentType type() {
        return PaymentType.BANK_TRANSFER;
    }

    @Override
    public void pay(Order order) {
        order.setPaymentReference(reference(order));
        order.setStatus(OrderStatus.PAID_BY_BANK_TRANSFER);
    }

    /** For example {@code BT-42-9F1C2A7B}: order id plus a random part that is hard to guess. */
    static String reference(Order order) {
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase(Locale.ROOT);
        return "BT-" + order.getId() + "-" + random;
    }
}
