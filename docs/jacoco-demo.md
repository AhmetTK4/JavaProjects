# jacoco-demo

Spring Boot project demonstrating JaCoCo code coverage. This module does not expose REST endpoints.

`mvn verify` generates the report under `target/site/jacoco/` and runs `jacoco:check`, which fails
the build when bundle line coverage is below 80% (`jacoco.minimum.line.coverage`).
