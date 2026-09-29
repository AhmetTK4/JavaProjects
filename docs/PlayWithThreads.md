# PlayWithThreads Service

Showcases multithreading techniques with Spring Boot endpoints. See [PlayWithThreads/README.md](../PlayWithThreads/README.md) for running Kafka and Mailpit locally.

## Endpoints

| Method | Path | Description |
|-------|------|-------------|
| GET | `/start-tasks` | Run `taskCount` (1–100, default 5) tasks in parallel on the `AsyncThread-` pool; the response completes asynchronously when all finish. |
| POST | `/send-message` | Send a message to Kafka topic. |
| POST | `/scheduler/task1` | Run task 1 now (it also runs every `app.scheduler.task1-rate`, default 30 s). |
| POST | `/scheduler/task2` | Run task 2 now (it also runs every `app.scheduler.task2-rate`, default 1 min). |
| POST | `/scheduler/notify` | E-mail the task 1 count to the operator-configured recipient. |
| GET | `/tasks/counts` | Current run counts of task 1 and task 2. |


## Configuration

Notification e-mails are sent only to `app.notification.recipient`
(environment variable `NOTIFICATION_RECIPIENT`). When it is empty, no e-mail is sent.
The service deliberately has no endpoint that accepts an arbitrary recipient, subject or body:
such an endpoint would let anyone send mail through the configured SMTP account.
