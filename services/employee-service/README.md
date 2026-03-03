# Employee Service

Hexagonal Architecture service responsible for employee lifecycle operations and employee events.

Planned responsibilities:
- Create, update, activate, and deactivate employees.
- Publish `EmployeeCreated`, `EmployeeActivated`, and `EmployeeDeactivated` events.

Planned stack:
- Spring Boot (Java 21)
- PostgreSQL (`employee_db`)
- Flyway migrations
- Kafka producer
