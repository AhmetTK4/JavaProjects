# Contributing

Use JDK 21 and Maven. Each project is independent: from the repository root, run `mvn -B -f <project>/pom.xml verify` for the example you change. CI verifies all ten examples.

- Read the project's documentation under `docs/` and any local README.
- For bugs, include the project name, reproduction steps, expected/actual behavior, and Java/Maven versions. Remove credentials from logs.
- Discuss substantial changes in an issue first. Keep PRs focused and include the test command and result.
- Add meaningful behavior tests for changed logic. Update the root index and documentation when adding an example.
- Keep learning simplifications explicit; do not present simulated deployment scripts as real deployments.

Maintenance is best-effort, with no guaranteed response time. See [SECURITY.md](SECURITY.md) for private security reporting.
