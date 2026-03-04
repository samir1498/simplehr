package com.samir.simplehr.employee.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Test
    void shouldCreateActiveEmployeeWithNormalizedEmail() {
        Employee employee = newEmployee(" Samir@Example.Com ");

        assertEquals(EmployeeStatus.ACTIVE, employee.getStatus());
        assertEquals("samir@example.com", employee.getEmail());
        assertEquals(new BigDecimal("150000.00"), employee.getSalary());
    }

    @Test
    void shouldAllowNonEmailLikeValueWhenFormatValidationIsDisabled() {
        Employee employee = newEmployee(" invalid-email ");

        assertEquals("invalid-email", employee.getEmail());
    }

    @Test
    void shouldAcceptReasonableEmailFormatsWithoutRegexOverfitting() {
        Employee employee = newEmployee("samir+backend@sub.example.co.uk");

        assertEquals("samir+backend@sub.example.co.uk", employee.getEmail());
    }

    @Test
    void shouldAllowClearlyBrokenDomainStructureWhenValidationIsDisabled() {
        Employee employee = newEmployee("samir@example..com");

        assertEquals("samir@example..com", employee.getEmail());
    }

    private Employee newEmployee(String email) {
        Instant now = Instant.now();
        return Employee.builder()
            .id(UUID.randomUUID())
            .fullName("Samir Bensalem")
            .email(email)
            .salary(new BigDecimal("150000"))
            .hireDate(LocalDate.of(2026, 3, 3))
            .status(EmployeeStatus.ACTIVE)
            .createdAt(now)
            .updatedAt(now)
            .version(0)
            .build();
    }
}
