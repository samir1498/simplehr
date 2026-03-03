package com.samir.simplehr.payroll.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

public final class PayrollAdjustment {
	private final UUID id;
	private final UUID employeeId;
	private final UUID leaveRequestId;
	private final BigDecimal amount;
	private final String reason;
	private final Instant createdAt;

	private PayrollAdjustment(UUID id, UUID employeeId, UUID leaveRequestId, BigDecimal amount, String reason, Instant createdAt) {
		this.id = requireUuid(id, "id");
		this.employeeId = requireUuid(employeeId, "employeeId");
		this.leaveRequestId = requireUuid(leaveRequestId, "leaveRequestId");
		this.amount = normalizeAmount(amount);
		this.reason = requireReason(reason);
		this.createdAt = requireInstant(createdAt, "createdAt");
	}

	public static PayrollAdjustment leaveDeduction(UUID employeeId, UUID leaveRequestId, BigDecimal amount) {
		return new PayrollAdjustment(UUID.randomUUID(), employeeId, leaveRequestId, amount, "LEAVE_DEDUCTION", Instant.now());
	}

	public UUID id() { return id; }
	public UUID employeeId() { return employeeId; }
	public UUID leaveRequestId() { return leaveRequestId; }
	public BigDecimal amount() { return amount; }
	public String reason() { return reason; }
	public Instant createdAt() { return createdAt; }

	private static UUID requireUuid(UUID value, String field) {
		if (value == null) throw new IllegalArgumentException(field + " is required");
		return value;
	}

	private static BigDecimal normalizeAmount(BigDecimal value) {
		if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("amount cannot be negative");
		}
		return value.setScale(2, RoundingMode.HALF_UP);
	}

	private static String requireReason(String value) {
		if (value == null || value.isBlank()) throw new IllegalArgumentException("reason is required");
		return value;
	}

	private static Instant requireInstant(Instant value, String field) {
		if (value == null) throw new IllegalArgumentException(field + " is required");
		return value;
	}
}
