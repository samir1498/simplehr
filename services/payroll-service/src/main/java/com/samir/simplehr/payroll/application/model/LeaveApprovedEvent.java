package com.samir.simplehr.payroll.application.model;

import java.time.LocalDate;
import java.util.UUID;

public record LeaveApprovedEvent(
		UUID eventId,
		UUID employeeId,
		UUID leaveRequestId,
		LocalDate startDate,
		LocalDate endDate
) {
}
