package com.samir.simplehr.employee.application.command;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateEmployeeCommand(
		String fullName,
		String email,
		BigDecimal salary,
		LocalDate hireDate
) {
}
