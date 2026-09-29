# [JavaProjects](https://github.com/AhmetTK4/JavaProjects)

This repository, **JavaProjects**, is a collection of Java projects developed by AhmetTK4.

## Table of Contents

- [Projects](#projects)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Projects

This repository includes various Java projects:

- **[GameOfLife](GameOfLife)**: Game of Life rules with behavior tests.
- **[jacoco-demo](jacoco-demo)**: Calculator examples and a JaCoCo coverage setup.
- **[StrategyDesignPattern](StrategyDesignPattern)**: Payment strategy selection with success and unknown-strategy tests.
- **[PlayWithCaches](https://github.com/AhmetTK4/JavaProjects/tree/main/PlayWithCaches)**: Explores caching mechanisms in Java.
- **[PlayWithGenerics](https://github.com/AhmetTK4/JavaProjects/tree/main/PlayWithGenerics)**: Provides examples on Java generics.
- **[PlayWithJson](https://github.com/AhmetTK4/JavaProjects/tree/main/PlayWithJson)**: Demonstrates methods for handling JSON data in Java.
- **[PlayWithStreams](https://github.com/AhmetTK4/JavaProjects/tree/main/PlayWithStreams)**: Offers examples using Java Stream API for data processing.
- **[PlayWithThreads](https://github.com/AhmetTK4/JavaProjects/tree/main/PlayWithThreads)**: Contains examples of multithreading applications in Java.
- **[ProxyDesignPattern](https://github.com/AhmetTK4/JavaProjects/tree/main/ProxyDesignPattern)**: Implements the Proxy design pattern in Java.
- **[kullanici-servis](https://github.com/AhmetTK4/JavaProjects/tree/main/kullanici-servis)**: A service application for user management.

## Service Documentation
For details of each web service, see the [docs](docs) folder.

## Installation

1. Clone this repository to your local machine:

   ```bash
   git clone https://github.com/AhmetTK4/JavaProjects.git
    ```
2. Navigate to the desired project:

   ```bash
   cd JavaProjects/ProjectName
   ```
3. Manage dependencies and build the project. If using Maven:
  
   ```bash
   mvn clean install
   ```
## Usage

Each project focuses on a specific Java topic or design pattern. Navigate to the project directory to examine and run the source code. For example, to run the PlayWithStreams project:

  ```bash
  cd JavaProjects/PlayWithStreams
  mvn spring-boot:run
  ```
From another terminal, try `curl http://localhost:8080/employees/names-uppercase`. See [the service documentation](docs/PlayWithStreams.md) for its other endpoints. Stop any other example using port 8080 before starting this one.

## Related writing

[Mastering Code Coverage with JaCoCo in Java 21 and Spring Boot](https://medium.com/@ahmettemelkundupoglu/mastering-code-coverage-with-jacoco-in-java-21-and-spring-boot-7e09ee26c039) — [runnable example](jacoco-demo).

## Verification

Each example is an independent Maven project. Run `mvn -B verify` from its directory. The GitHub Actions matrix verifies all ten Maven projects on JDK 21 and JDK 25 on pull requests and `main` (kullanici-servis runs on JDK 21 only until its Quarkus version supports Java 25). Tests and builds do not represent a production deployment. The scripts under `scripts/` are historical deployment simulations and are not run by CI.

To run CI manually, open **Actions → Verify Java examples → Run workflow**.
Open a completed run's **Artifacts** section to download `build-<project>-<attempt>`
(JDK 21 builds) and `reports-<project>-jdk<version>-<attempt>` archives (retained for 14 days).
Builds are uploaded only after successful verification; test reports are retained even when tests fail.
The Quarkus build includes the entire `quarkus-app` directory: keep its libraries
together with `quarkus-run.jar`. These are Maven build outputs, not deployments;
the existing packaging of each example is preserved.

The Spring Boot web examples use Boot 4.1.1 and, where applicable, Springdoc
3.1.1. PlayWithThreads also uses Spring Kafka 4.1.1 and the Boot Kafka starter.
The migration updates the JPA/MVC test modules and uses MockitoBean for the
mock mail sender. The thread example's tests disable Kafka listeners and need
no real email credentials or broker. Running its mail/Kafka features normally
still requires the configured services.

Dependabot keeps minor/patch updates enabled. Future Boot, Kafka and Springdoc
major upgrades require a coordinated migration of their companion libraries and
test APIs. The September 2026 isolated major-update failures were resolved by
integrating those updates together; historical failed runs remain in Actions.

For contribution steps, see [CONTRIBUTING.md](CONTRIBUTING.md). For sensitive vulnerability reports, see [SECURITY.md](SECURITY.md).

## Contributing

Contributions are welcome! Please open an issue to discuss the changes you wish to make. Then, submit a pull request to contribute.

## License

This project is licensed under the MIT License.

**[License](https://github.com/AhmetTK4/JavaProjects/blob/main/LICENSE)**
