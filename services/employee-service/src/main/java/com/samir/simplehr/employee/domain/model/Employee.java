package com.samir.simplehr.employee.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public final class Employee {

    private final UUID id;
    private final String fullName;
    private final String email;
    private final BigDecimal salary;
    private final LocalDate hireDate;
    private final EmployeeStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final long version;

    private Employee(
        UUID id,
        String fullName,
        String email,
        BigDecimal salary,
        LocalDate hireDate,
        EmployeeStatus status,
        Instant createdAt,
        Instant updatedAt,
        long version
    ) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.fullName = normalizeFullName(fullName);
        this.email = normalizeEmail(email);
        this.salary = normalizeSalary(salary);
        this.hireDate = Objects.requireNonNull(
            hireDate,
            "hireDate is required"
        );
        this.status = Objects.requireNonNull(status, "status is required");
        this.createdAt = Objects.requireNonNull(
            createdAt,
            "createdAt is required"
        );
        this.updatedAt = Objects.requireNonNull(
            updatedAt,
            "updatedAt is required"
        );
        this.version = version;
    }

    public static Employee rehydrate(
        UUID id,
        String fullName,
        String email,
        BigDecimal salary,
        LocalDate hireDate,
        EmployeeStatus status,
        Instant createdAt,
        Instant updatedAt,
        long version
    ) {
        return new Employee(
            id,
            fullName,
            email,
            salary,
            hireDate,
            status,
            createdAt,
            updatedAt,
            version
        );
    }

    public Employee activate() {
        if (status == EmployeeStatus.ACTIVE) {
            return this;
        }
        return new Employee(
            id,
            fullName,
            email,
            salary,
            hireDate,
            EmployeeStatus.ACTIVE,
            createdAt,
            Instant.now(),
            version
        );
    }

    public Employee deactivate() {
        if (status == EmployeeStatus.INACTIVE) {
            return this;
        }
        return new Employee(
            id,
            fullName,
            email,
            salary,
            hireDate,
            EmployeeStatus.INACTIVE,
            createdAt,
            Instant.now(),
            version
        );
    }

    private static String normalizeFullName(String fullName) {
        return Objects.requireNonNull(fullName, "fullName is required").trim();
    }

    private static String normalizeEmail(String email) {
        return Objects.requireNonNull(email, "email is required")
            .trim()
            .toLowerCase(Locale.ROOT);
    }

    private static BigDecimal normalizeSalary(BigDecimal salary) {
        BigDecimal normalizedSalary = Objects.requireNonNull(
            salary,
            "salary is required"
        ).setScale(2, RoundingMode.HALF_UP);
        if (normalizedSalary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                "salary must be greater than zero"
            );
        }
        return normalizedSalary;
    }
}
