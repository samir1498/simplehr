package com.samir.simplehr.leave.presentation.web.dto;

public record ReviewLeaveRequest(
		String reviewerId,
		String rejectionReason
) {
}
