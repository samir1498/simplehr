package com.samir.simplehr.leave.presentation.web.dto;

import jakarta.validation.constraints.NotBlank;

public record RejectLeaveRequest(
		@NotBlank(message = "reviewerId is required")
		String reviewerId,
		@NotBlank(message = "rejectionReason is required")
		String rejectionReason
) {
}
