# SimpleHR Project Specification

## 1. Purpose
SimpleHR is a showcase distributed Human Resources (HR) and payroll software-as-a-service (SaaS) project designed to demonstrate:
- Microservices design with synchronous and asynchronous communication.
- Domain-Driven Design (DDD) with clear bounded contexts.
- Clean Architecture and Hexagonal Architecture in practical service implementations.
- Production-minded testing, reliability, and maintainability practices.

## 2. Source and Context
This specification is derived from an earlier brainstorming transcript that was previously stored in `gpt_spec.md`.

It originated as interview preparation content for a Spring Boot backend role focused on microservices, PostgreSQL, Kafka, security, and testing. The transcript has been normalized here into a formal project document.

## 3. Domain Scope
Core capabilities:
- Employee management
- Leave management
- Payroll generation
- Authentication and role-based authorization

Initial actor roles:
- `HR_ADMIN`
- `MANAGER`
- `EMPLOYEE`

## 4. Target Technical Stack
- Java 21
- Spring Boot
- PostgreSQL (database per service)
- Apache Kafka (event-driven communication)
- Docker and Docker Compose
- Flyway (database migrations)
- JUnit 5, AssertJ, Mockito, Testcontainers

## 5. Service Boundaries
### Employee Service (Hexagonal)
Responsibilities:
- Create, update, activate, and deactivate employees.
- Publish employee lifecycle events.

Owns:
- `Employee` aggregate and related value objects.

Publishes:
- `EmployeeCreated`
- `EmployeeActivated`
- `EmployeeDeactivated`

### Leave Service (Clean Architecture)
Responsibilities:
- Submit leave requests.
- Approve or reject leave requests.
- Enforce leave balance and overlap rules.

Owns:
- `LeaveRequest` aggregate.

Publishes:
- `LeaveRequested`
- `LeaveApproved`
- `LeaveRejected`

### Payroll Service (Hexagonal + Event-Driven)
Responsibilities:
- Generate monthly payroll.
- Recalculate payroll based on leave approvals.
- Consume employee and leave events.

Consumes:
- `EmployeeCreated`
- `LeaveApproved`

Publishes:
- `PayrollGenerated`

### Auth Service (Optional)
Responsibilities:
- Provide authentication and token issuance/validation.
- Enforce role-based access controls.

Preferred approach:
- Keycloak integration with JWT.

## 6. Communication Model
Synchronous:
- REST APIs via gateway-to-service and service-to-service calls where needed.

Asynchronous:
- Kafka topics:
  - `employee.events`
  - `leave.events`
  - `payroll.events`

Event envelope contract:
```json
{
  "eventId": "uuid",
  "eventType": "EmployeeCreated",
  "occurredAt": "2026-03-03T10:00:00Z",
  "payload": {}
}
```

## 7. Quality Attributes
- Maintainability: explicit architecture boundaries and clear module ownership.
- Reliability: idempotent consumers and outbox-friendly event handling.
- Security: JWT validation, role checks, and least-privilege access.
- Testability: domain-first unit tests and containerized integration tests.

## 8. Non-Goals for MVP
- Full multi-tenancy
- Full command query responsibility segregation (CQRS)
- Full distributed saga orchestration engine

These may be added after MVP stabilization.
