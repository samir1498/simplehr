package com.samir.simplehr.payroll.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

public final class PayrollProfile {
	private final UUID id;
	private final UUID employeeId;
	private final BigDecimal baseSalary;
	private final boolean active;
	private final Instant createdAt;
	private final Instant updatedAt;
	private final long version;

	private PayrollProfile(
			UUID id,
			UUID employeeId,
			BigDecimal baseSalary,
			boolean active,
			Instant createdAt,
			Instant updatedAt,
			long version
	) {
		this.id = requireId(id);
		this.employeeId = requireEmployeeId(employeeId);
		this.baseSalary = normalizeSalary(baseSalary);
		this.active = active;
		this.createdAt = requireInstant(createdAt, "createdAt");
		this.updatedAt = requireInstant(updatedAt, "updatedAt");
		this.version = version;
	}

	public static PayrollProfile create(UUID employeeId, BigDecimal baseSalary) {
		Instant now = Instant.now();
		return new PayrollProfile(UUID.randomUUID(), employeeId, baseSalary, true, now, now, 0);
	}

	public static PayrollProfile rehydrate(
			UUID id,
			UUID employeeId,
			BigDecimal baseSalary,
			boolean active,
			Instant createdAt,
			Instant updatedAt,
			long version
	) {
		return new PayrollProfile(id, employeeId, baseSalary, active, createdAt, updatedAt, version);
	}

	public PayrollProfile updateBaseSalary(BigDecimal newSalary) {
		return new PayrollProfile(id, employeeId, newSalary, active, createdAt, Instant.now(), version);
	}

	public UUID id() { return id; }
	public UUID employeeId() { return employeeId; }
	public BigDecimal baseSalary() { return baseSalary; }
	public boolean active() { return active; }
	public Instant createdAt() { return createdAt; }
	public Instant updatedAt() { return updatedAt; }
	public long version() { return version; }

	private static UUID requireId(UUID value) {
		if (value == null) throw new IllegalArgumentException("id is required");
		return value;
	}

	private static UUID requireEmployeeId(UUID value) {
		if (value == null) throw new IllegalArgumentException("employeeId is required");
		return value;
	}

	private static Instant requireInstant(Instant value, String field) {
		if (value == null) throw new IllegalArgumentException(field + " is required");
		return value;
	}

	private static BigDecimal normalizeSalary(BigDecimal salary) {
		if (salary == null || salary.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("baseSalary must be greater than zero");
		}
		return salary.setScale(2, RoundingMode.HALF_UP);
	}
}
