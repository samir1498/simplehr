package com.samir.simplehr.payroll.application.model;

import java.math.BigDecimal;
import java.util.UUID;

public record EmployeeCreatedEvent(
		UUID eventId,
		UUID employeeId,
		BigDecimal salary
) {
}
