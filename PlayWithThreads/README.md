# PlayWithThreads

Spring Boot examples of `@Async` thread pools, `@Scheduled` jobs, an AOP timing aspect,
Kafka messaging and asynchronous e-mail. Endpoint reference: [docs/PlayWithThreads.md](../docs/PlayWithThreads.md).

## Run locally

```bash
docker compose up -d          # Kafka (KRaft) on 9092, Mailpit SMTP on 1025 / UI on 8025
NOTIFICATION_RECIPIENT=ops@example.com mvn spring-boot:run
```

Try it:

```bash
curl 'http://localhost:8080/start-tasks?taskCount=10'   # 10 parallel tasks on the AsyncThread- pool
curl http://localhost:8080/tasks/counts                  # counters of the scheduled jobs
curl -X POST 'http://localhost:8080/send-message?message=Merhaba%20Kafka'
curl -X POST http://localhost:8080/scheduler/notify      # e-mail appears in Mailpit at http://localhost:8025
```

Swagger UI: http://localhost:8080/swagger-ui.html

## Configuration

| Property / environment variable | Default | Purpose |
|---|---|---|
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` | Kafka brokers |
| `MAIL_HOST`, `MAIL_PORT` | `localhost`, `1025` | SMTP server (Mailpit) |
| `EMAIL_USERNAME`, `EMAIL_PASSWORD`, `MAIL_SMTP_AUTH`, `MAIL_STARTTLS` | empty, empty, `false`, `false` | SMTP credentials and security |
| `NOTIFICATION_RECIPIENT` | empty (no mail sent) | Recipient of `/scheduler/notify` |
| `app.scheduler.task1-rate`, `app.scheduler.task2-rate` | `PT30S`, `PT1M` | Rates of the scheduled jobs |

To send through Gmail instead, use an [app password](https://support.google.com/accounts/answer/185833):
`MAIL_HOST=smtp.gmail.com MAIL_PORT=587 MAIL_SMTP_AUTH=true MAIL_STARTTLS=true EMAIL_USERNAME=... EMAIL_PASSWORD=...`.
Never commit these values.

## Virtual threads (Java 21)

`GET /virtual-threads/compare?tasks=1000&blockingMillis=100&platformPoolSize=20` runs the same
blocking tasks on a fixed pool of platform threads and on `Executors.newVirtualThreadPerTaskExecutor()`
and returns both durations. With the defaults the platform pool needs about 5 s (50 rounds of 100 ms),
while virtual threads finish in about 100 ms because a blocked virtual thread does not occupy an OS thread.

Spring Boot can also run request handling, `@Async` and `@Scheduled` on virtual threads with
`spring.threads.virtual.enabled=true`. This project keeps it off so the `AsyncThread-` pool in
`AsyncConfig` remains visible; try enabling it and compare the thread names in the logs.
Virtual threads help with blocking I/O, not CPU-bound work.
