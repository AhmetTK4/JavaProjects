# JaCoCo demo

Companion article: [Mastering Code Coverage with JaCoCo in Java 21 and Spring Boot](https://medium.com/@ahmettemelkundupoglu/mastering-code-coverage-with-jacoco-in-java-21-and-spring-boot-7e09ee26c039).

Run `mvn verify` with Java 21 or newer and Maven. It:

- compiles for Java 21 (`maven.compiler.release`, set by the Spring Boot parent from `java.version`),
- writes the coverage report to `target/site/jacoco/index.html`,
- fails the build if line coverage drops below `jacoco.minimum.line.coverage` (80%),
- produces an executable jar: `java -jar target/jacoco-demo-1.0.0.jar`.

`DemoApplication` is excluded from coverage because it only starts Spring.
