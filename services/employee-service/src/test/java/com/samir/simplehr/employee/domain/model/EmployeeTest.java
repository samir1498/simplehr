package com.samir.simplehr.employee.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmployeeTest {

	@Test
	void shouldCreateActiveEmployeeWithNormalizedEmail() {
		Employee employee = Employee.create(
				"Samir Bensalem",
				" Samir@Example.Com ",
				new BigDecimal("150000"),
				LocalDate.of(2026, 3, 3)
		);

		assertEquals(EmployeeStatus.ACTIVE, employee.status());
		assertEquals("samir@example.com", employee.email());
		assertEquals(new BigDecimal("150000.00"), employee.salary());
	}

	@Test
	void shouldRejectInvalidEmail() {
		assertThrows(IllegalArgumentException.class, () -> Employee.create(
				"Samir Bensalem",
				"invalid-email",
				new BigDecimal("150000"),
				LocalDate.of(2026, 3, 3)
		));
	}
}
