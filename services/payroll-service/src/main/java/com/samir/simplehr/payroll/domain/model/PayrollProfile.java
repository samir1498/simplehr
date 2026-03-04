package com.samir.simplehr.payroll.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

public record PayrollProfile(
		UUID id,
		UUID employeeId,
		BigDecimal baseSalary,
		boolean active,
		Instant createdAt,
		Instant updatedAt,
		long version
) {
	public PayrollProfile {
		if (id == null) {
			throw new IllegalArgumentException("id is required");
		}
		if (employeeId == null) {
			throw new IllegalArgumentException("employeeId is required");
		}
		if (createdAt == null) {
			throw new IllegalArgumentException("createdAt is required");
		}
		if (updatedAt == null) {
			throw new IllegalArgumentException("updatedAt is required");
		}
		if (baseSalary == null || baseSalary.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("baseSalary must be greater than zero");
		}
		baseSalary = baseSalary.setScale(2, RoundingMode.HALF_UP);
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
}
