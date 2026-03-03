# MVP Delivery Plan

## Phase 1: Repository Bootstrap
- Create service directories and shared documentation.
- Add Docker Compose for PostgreSQL and Kafka.
- Add base Spring Boot setup per service.

## Phase 2: Employee Service
- Implement `Employee` aggregate and lifecycle use cases.
- Expose REST endpoints.
- Persist using PostgreSQL + Flyway.
- Publish employee events.
- Add unit and integration tests.

## Phase 3: Leave Service
- Implement leave request and approval workflows.
- Enforce overlap and allowance constraints.
- Persist and expose REST endpoints.
- Publish leave events.
- Add tests for domain rules and adapters.

## Phase 4: Payroll Service
- Consume `EmployeeCreated` and `LeaveApproved` events.
- Implement payroll profile bootstrap and adjustment logic.
- Publish `PayrollGenerated` events.
- Add idempotency and integration tests.

## Phase 5: Security and Hardening
- Integrate Keycloak or minimal JWT auth.
- Apply role-based authorization in each service.
- Add API contracts and event contract checks.
- Add observability baseline (health/readiness, logs, tracing hooks).
