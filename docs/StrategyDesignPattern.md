# StrategyDesignPattern Service

Example project implementing the Strategy design pattern for order payments.

Each `PaymentStrategy` bean declares the `PaymentType` it handles. `PaymentServiceImpl` collects
all strategies into an `EnumMap` at startup (two strategies for the same type fail fast) and picks
the one matching the order's payment type. Adding a payment method means adding an enum constant
and one strategy class; the service does not change.

## Endpoints

| Method | Path | Description |
|-------|------|-------------|
| POST | `/api/orders?type={CREDIT_CARD\|PAYPAL}&amount={amount}` | Create an order. `amount` must be positive with at most two decimals. Returns `201 Created`. |
| POST | `/api/orders/{id}/pay` | Pay the order with the strategy for its payment type. `409` if it is already paid. |
| GET | `/api/orders/{id}` | Get an order. `404` if it does not exist. |

Errors are returned as problem details: `400` unknown payment type or invalid amount,
`404` order not found, `409` order already paid.

```bash
curl -X POST 'http://localhost:8080/api/orders?type=PAYPAL&amount=19.99'
curl -X POST 'http://localhost:8080/api/orders/3/pay'
```
