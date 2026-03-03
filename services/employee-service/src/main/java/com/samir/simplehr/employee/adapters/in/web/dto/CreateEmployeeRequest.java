package com.samir.simplehr.employee.adapters.in.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateEmployeeRequest(
		@NotBlank(message = "fullName is required")
		String fullName,
		@NotBlank(message = "email is required")
		@Email(message = "email has invalid format")
		String email,
		@NotNull(message = "salary is required")
		@DecimalMin(value = "0.01", message = "salary must be greater than zero")
		BigDecimal salary,
		@NotNull(message = "hireDate is required")
		LocalDate hireDate
) {
}
