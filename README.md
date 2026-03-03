# SimpleHR

SimpleHR is a microservices-based Human Resources (HR) and payroll showcase project.

## Current Status
- Documentation baseline is in `docs/`.
- Phase 1 infrastructure scaffold is available in `infra/docker-compose.yml`.
- Service directories are bootstrapped under `services/`.

## Quick Start (Infrastructure)
From repository root:

```bash
docker compose -f infra/docker-compose.yml up -d
```

Stop and remove containers:

```bash
docker compose -f infra/docker-compose.yml down -v
```

## Service Directories
- `services/employee-service`
- `services/leave-service`
- `services/payroll-service`

## Next Implementation Step
Build `employee-service` first with:
- Hexagonal architecture modules
- Flyway migrations
- REST API for employee lifecycle
- Kafka event publishing
- Unit and integration tests
