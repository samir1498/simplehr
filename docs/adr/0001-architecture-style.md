# ADR 0001: Mixed Clean and Hexagonal Service Architecture

## Status
Accepted

## Context
The project goal includes demonstrating both Clean Architecture and Hexagonal Architecture in realistic microservices while keeping domain logic explicit and testable.

## Decision
Use a mixed architecture approach by bounded context:
- Hexagonal Architecture for Employee and Payroll services.
- Clean Architecture for Leave service.

## Consequences
Positive:
- Enables clear interview-ready comparison of the two architectural styles.
- Encourages strict dependency direction and framework isolation.
- Supports high testability of domain rules.

Trade-offs:
- Increased cognitive load from two patterns in one project.
- Requires strict documentation and review discipline to avoid inconsistent boundaries.
