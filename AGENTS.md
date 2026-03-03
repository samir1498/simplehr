# Repository Guidelines

## Scope
These guidelines apply to the `simplehr` repository. Keep changes safe, explicit, and aligned with the architecture in `docs/project-spec.md`.

## Project Structure & Module Organization
Current state is spec-first:
- `docs/project-spec.md` defines domain boundaries, services, and architecture goals.

Target implementation layout:
- `services/employee-service`
- `services/leave-service`
- `services/payroll-service`
- `services/auth-service` (optional)
- `infra/` for `docker-compose.yml` and local environment config
- `contracts/` for API and Kafka event contracts
- `docs/` for supporting design notes

Per service, use one style consistently:
- Hexagonal: `domain/`, `application/`, `adapters/in`, `adapters/out`
- Clean: `domain/`, `application/`, `infrastructure/`, `presentation/`

## Engineering Workflow (Applicable Rules)
- Verify feasibility and framework/API constraints before coding.
- Share a brief plan before substantial code changes.
- Respect existing conventions; do not add dependencies or new patterns unless clearly required.
- Assume running services are already started; do not start/restart dev servers unless explicitly requested.
- Do not leave placeholders (`TODO`, stubs, or "existing code" markers) in delivered changes.
- Frontend rule (when frontend is added): use the Container/Presentation pattern. Containers own hooks and side effects; presentational components remain pure and props-driven.

## Build, Test, and Development Commands
From each service (Maven wrapper expected):
- `./mvnw clean test` - unit and integration tests
- `./mvnw spring-boot:run` - run a service locally
- `./mvnw spotless:apply` - format code (if configured)

From repo root (when infra exists):
- `docker compose up -d`
- `docker compose down -v`

## Coding Style & Naming Conventions
Use Java 21, UTF-8, and 4-space indentation. Keep domain logic framework-agnostic. Naming:
- Classes: `PascalCase`
- Methods/fields: `camelCase`
- Constants/enums: `UPPER_SNAKE_CASE`
- Events: past tense (`EmployeeCreated`, `LeaveApproved`)

## Testing Guidelines
Use JUnit 5, AssertJ, Mockito, and Testcontainers (PostgreSQL + Kafka). Prefer:
- domain-first unit tests,
- integration tests for adapters and persistence,
- idempotency tests for Kafka consumers,
- contract tests for inter-service integration when applicable.

## Safety & Git Protocols
- Never run destructive git commands (`git reset --hard`, `git clean -fd`) without explicit confirmation.
- Before complex changes, offer a feature branch (when repo git is initialized).
- Do not delete or overwrite non-code assets without permission.

## Commit & Pull Request Guidelines
If/when git is initialized, use Conventional Commits (`feat:`, `fix:`, `test:`, `chore:`). Keep commits scoped by concern (domain, API, infra, tests). PRs must include intent, test evidence, and contract changes for API/event updates.
