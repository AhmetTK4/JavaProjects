# PlayWithThreads Service

Showcases multithreading techniques with Spring Boot endpoints.

## Endpoints

| Method | Path | Description |
|-------|------|-------------|
| GET | `/start-tasks` | Trigger asynchronous tasks. Accepts `taskCount` query param. |
| POST | `/send-message` | Send a message to Kafka topic. |
| POST | `/scheduler/task1` | Trigger scheduled task 1. |
| POST | `/scheduler/task2` | Trigger scheduled task 2. |
| POST | `/scheduler/notify` | E-mail the task 1 count to the operator-configured recipient. |
| GET | `/tasks/counts` | Retrieve scheduler task counts. |


## Configuration

Notification e-mails are sent only to `app.notification.recipient`
(environment variable `NOTIFICATION_RECIPIENT`). When it is empty, no e-mail is sent.
The service deliberately has no endpoint that accepts an arbitrary recipient, subject or body:
such an endpoint would let anyone send mail through the configured SMTP account.
