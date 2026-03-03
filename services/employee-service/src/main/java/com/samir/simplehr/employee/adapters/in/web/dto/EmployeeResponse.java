package com.samir.simplehr.employee.adapters.in.web.dto;

import com.samir.simplehr.employee.application.model.EmployeeResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record EmployeeResponse(
		UUID id,
		String fullName,
		String email,
		BigDecimal salary,
		LocalDate hireDate,
		String status
) {
	public static EmployeeResponse from(EmployeeResult employee) {
		return new EmployeeResponse(
				employee.id(),
				employee.fullName(),
				employee.email(),
				employee.salary(),
				employee.hireDate(),
				employee.status().name()
		);
	}
}
