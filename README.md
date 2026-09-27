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

## Verification

Each example is an independent Maven project. Run `mvn -B verify` from its directory. The GitHub Actions matrix verifies all ten Maven projects on pull requests and `main`. Tests and builds do not represent a production deployment. The scripts under `scripts/` are historical deployment simulations and are not run by CI.

Spring Boot 3.3 examples use Springdoc 2.6.x according to the [upstream compatibility matrix](https://springdoc.org/v2/#what-is-the-compatibility-matrix-of-springdoc-openapi-with-spring-boot). Upgrade these together; Springdoc 3.x targets Spring Boot 4. The PlayWithThreads context test uses a mocked mail sender and disables Kafka listeners, so it needs no real email credentials or broker. Running its mail/Kafka features normally still requires the configured services.

PlayWithThreads uses Spring Boot 3.5.13, springdoc 2.9.1 and Spring Kafka 3.3.16 to address GHSA-53w6-v7cv-fc9h, GHSA-xq69-5h5v-x9x4 and GHSA-xvfq-4q6q-gxx7. Its context test runs without a live mail or Kafka service.

For contribution steps, see [CONTRIBUTING.md](CONTRIBUTING.md). For sensitive vulnerability reports, see [SECURITY.md](SECURITY.md).

## Contributing

Contributions are welcome! Please open an issue to discuss the changes you wish to make. Then, submit a pull request to contribute.

## License

This project is licensed under the MIT License.

**[License](https://github.com/AhmetTK4/JavaProjects/blob/main/LICENSE)**
