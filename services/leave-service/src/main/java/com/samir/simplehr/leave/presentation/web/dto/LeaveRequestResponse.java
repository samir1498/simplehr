package com.samir.simplehr.leave.presentation.web.dto;

import com.samir.simplehr.leave.application.model.LeaveRequestResult;

import java.time.LocalDate;
import java.util.UUID;

public record LeaveRequestResponse(
		UUID id,
		UUID employeeId,
		LocalDate startDate,
		LocalDate endDate,
		String status,
		String reviewedBy,
		String rejectionReason
) {
	public static LeaveRequestResponse from(LeaveRequestResult leaveRequestResult) {
		return new LeaveRequestResponse(
				leaveRequestResult.id(),
				leaveRequestResult.employeeId(),
				leaveRequestResult.startDate(),
				leaveRequestResult.endDate(),
				leaveRequestResult.status().name(),
				leaveRequestResult.reviewedBy(),
				leaveRequestResult.rejectionReason()
		);
	}
}
