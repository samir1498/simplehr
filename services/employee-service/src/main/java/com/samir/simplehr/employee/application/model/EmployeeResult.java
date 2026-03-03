package com.samir.simplehr.employee.application.model;

import com.samir.simplehr.employee.domain.model.Employee;
import com.samir.simplehr.employee.domain.model.EmployeeStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record EmployeeResult(
		UUID id,
		String fullName,
		String email,
		BigDecimal salary,
		LocalDate hireDate,
		EmployeeStatus status
) {
	public static EmployeeResult from(Employee employee) {
		return new EmployeeResult(
				employee.id(),
				employee.fullName(),
				employee.email(),
				employee.salary(),
				employee.hireDate(),
				employee.status()
		);
	}
}
