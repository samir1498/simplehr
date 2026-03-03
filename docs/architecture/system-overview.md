# System Overview

## Architectural Style
SimpleHR uses a modular microservice architecture with explicit bounded contexts.

- Employee Service: Hexagonal Architecture
- Leave Service: Clean Architecture
- Payroll Service: Hexagonal Architecture with event-driven processing
- Auth Service: externalized identity provider (preferred: Keycloak)

## High-Level Topology
- API Gateway routes external requests.
- Each service owns its own PostgreSQL schema/database.
- Kafka provides asynchronous integration.
- No direct database sharing between services.

## Bounded Contexts
### Employee Context
Owns identity and lifecycle of employees.

### Leave Context
Owns leave policy rules and leave state transitions.

### Payroll Context
Owns payroll calculations and generated payroll records.

### Identity Context
Owns authentication and role resolution.

## Core Integration Flows
1. Employee onboarding
- Employee Service stores employee and emits `EmployeeCreated`.
- Payroll Service consumes event and initializes payroll profile.

2. Leave approval impact
- Leave Service approves leave and emits `LeaveApproved`.
- Payroll Service consumes event and recalculates payroll.

## Reliability Patterns
- Idempotent event consumers.
- Message contract versioning.
- Outbox pattern compatibility for service-local transaction consistency.

## Security Model
- JWT-based authentication.
- Role-based authorization (`HR_ADMIN`, `MANAGER`, `EMPLOYEE`).
- Service-level validation for privileged operations.
