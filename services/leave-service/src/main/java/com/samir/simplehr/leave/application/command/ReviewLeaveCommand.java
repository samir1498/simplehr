package com.samir.simplehr.leave.application.command;

public record ReviewLeaveCommand(
		String role,
		String reviewerId,
		String rejectionReason
) {
}
