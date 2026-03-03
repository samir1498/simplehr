package com.samir.simplehr.leave.presentation.web.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record RequestLeaveRequest(
		@NotNull(message = "employeeId is required")
		UUID employeeId,
		@NotNull(message = "startDate is required")
		LocalDate startDate,
		@NotNull(message = "endDate is required")
		LocalDate endDate
) {
}
