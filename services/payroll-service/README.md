# Payroll Service

Hexagonal Architecture service responsible for payroll generation and event-driven recalculation.

Planned responsibilities:
- Generate monthly payroll.
- Consume `EmployeeCreated` and `LeaveApproved` events.
- Publish `PayrollGenerated` events.

Planned stack:
- Spring Boot (Java 21)
- PostgreSQL (`payroll_db`)
- Flyway migrations
- Kafka consumer and producer
