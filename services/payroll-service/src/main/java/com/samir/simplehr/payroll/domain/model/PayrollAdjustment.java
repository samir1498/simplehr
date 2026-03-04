package com.samir.simplehr.payroll.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

public record PayrollAdjustment(
		UUID id,
		UUID employeeId,
		UUID leaveRequestId,
		BigDecimal amount,
		String reason,
		Instant createdAt
) {
	public PayrollAdjustment {
		if (id == null) {
			throw new IllegalArgumentException("id is required");
		}
		if (employeeId == null) {
			throw new IllegalArgumentException("employeeId is required");
		}
		if (leaveRequestId == null) {
			throw new IllegalArgumentException("leaveRequestId is required");
		}
		if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("amount cannot be negative");
		}
		if (reason == null || reason.isBlank()) {
			throw new IllegalArgumentException("reason is required");
		}
		if (createdAt == null) {
			throw new IllegalArgumentException("createdAt is required");
		}
		amount = amount.setScale(2, RoundingMode.HALF_UP);
	}

	public static PayrollAdjustment leaveDeduction(UUID employeeId, UUID leaveRequestId, BigDecimal amount) {
		return new PayrollAdjustment(UUID.randomUUID(), employeeId, leaveRequestId, amount, "LEAVE_DEDUCTION", Instant.now());
	}
}
