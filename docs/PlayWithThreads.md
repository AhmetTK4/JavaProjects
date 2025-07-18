# PlayWithThreads Service

Showcases multithreading techniques with Spring Boot endpoints.

## Endpoints

| Method | Path | Description |
|-------|------|-------------|
| GET | `/start-tasks` | Trigger asynchronous tasks. Accepts `taskCount` query param. |
| POST | `/send-message` | Send a message to Kafka topic. |
| POST | `/scheduler/task1` | Trigger scheduled task 1. |
| POST | `/scheduler/task2` | Trigger scheduled task 2. |
| POST | `/scheduler/notify` | Notify when tasks complete. |
| POST | `/scheduler/send-email` | Send an email with `to`, `subject` and `text` parameters. |
| GET | `/tasks/counts` | Retrieve scheduler task counts. |

