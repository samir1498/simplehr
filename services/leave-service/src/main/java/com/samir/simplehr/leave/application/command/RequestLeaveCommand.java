package com.samir.simplehr.leave.application.command;

import java.time.LocalDate;
import java.util.UUID;

public record RequestLeaveCommand(
		UUID employeeId,
		LocalDate startDate,
		LocalDate endDate
) {
}
