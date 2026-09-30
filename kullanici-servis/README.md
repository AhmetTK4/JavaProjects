# kullanici-servis

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

Endpoint reference: [docs/kullanici-servis.md](../docs/kullanici-servis.md). The service listens on port `9095`.

## Configuration

| Profile | Database |
|---|---|
| dev (`mvn quarkus:dev`) | PostgreSQL started automatically by [Dev Services](https://quarkus.io/guides/databases-dev-services) (requires Docker) |
| test (`mvn verify`) | In-memory H2, no Docker needed |
| prod (`java -jar ...`, container) | `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` environment variables (required, no defaults) |

The schema is created by Flyway from `src/main/resources/db/migration` at startup.

```shell script
DB_URL=jdbc:postgresql://localhost:5432/postgres DB_USERNAME=postgres DB_PASSWORD=... \
  java -jar target/quarkus-app/quarkus-run.jar
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
mvn compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
mvn package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
mvn package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
mvn package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
mvn package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/kullanici-servis-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST with Jackson ([guide](https://quarkus.io/guides/rest-json))
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache))
- Flyway ([guide](https://quarkus.io/guides/flyway))
- Validation ([guide](https://quarkus.io/guides/validation))
- SmallRye Health ([guide](https://quarkus.io/guides/smallrye-health))
