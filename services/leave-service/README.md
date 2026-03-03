# Leave Service

Clean Architecture service responsible for leave workflows and leave policy rules.

Planned responsibilities:
- Submit leave requests.
- Approve and reject requests.
- Enforce overlap and allowance constraints.

Planned stack:
- Spring Boot (Java 21)
- PostgreSQL (`leave_db`)
- Flyway migrations
- Kafka producer
