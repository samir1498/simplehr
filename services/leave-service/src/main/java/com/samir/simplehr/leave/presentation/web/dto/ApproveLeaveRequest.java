package com.samir.simplehr.leave.presentation.web.dto;

import jakarta.validation.constraints.NotBlank;

public record ApproveLeaveRequest(
		@NotBlank(message = "reviewerId is required")
		String reviewerId
) {
}
