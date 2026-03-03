package com.samir.simplehr.employee.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

public final class Employee {
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

	private final UUID id;
	private final String fullName;
	private final String email;
	private final BigDecimal salary;
	private final LocalDate hireDate;
	private final EmployeeStatus status;
	private final Instant createdAt;
	private final Instant updatedAt;
	private final long version;

	private Employee(
			UUID id,
			String fullName,
			String email,
			BigDecimal salary,
			LocalDate hireDate,
			EmployeeStatus status,
			Instant createdAt,
			Instant updatedAt,
			long version
	) {
		this.id = requireId(id);
		this.fullName = normalizeFullName(fullName);
		this.email = normalizeEmail(email);
		this.salary = normalizeSalary(salary);
		this.hireDate = requireHireDate(hireDate);
		this.status = requireStatus(status);
		this.createdAt = requireInstant(createdAt, "createdAt");
		this.updatedAt = requireInstant(updatedAt, "updatedAt");
		this.version = version;
	}

	public static Employee create(String fullName, String email, BigDecimal salary, LocalDate hireDate) {
		Instant now = Instant.now();
		return new Employee(
				UUID.randomUUID(),
				fullName,
				email,
				salary,
				hireDate,
				EmployeeStatus.ACTIVE,
				now,
				now,
				0
		);
	}

	public static Employee rehydrate(
			UUID id,
			String fullName,
			String email,
			BigDecimal salary,
			LocalDate hireDate,
			EmployeeStatus status,
			Instant createdAt,
			Instant updatedAt,
			long version
	) {
		return new Employee(id, fullName, email, salary, hireDate, status, createdAt, updatedAt, version);
	}

	public Employee activate() {
		if (status == EmployeeStatus.ACTIVE) {
			return this;
		}
		return new Employee(id, fullName, email, salary, hireDate, EmployeeStatus.ACTIVE, createdAt, Instant.now(), version);
	}

	public Employee deactivate() {
		if (status == EmployeeStatus.INACTIVE) {
			return this;
		}
		return new Employee(id, fullName, email, salary, hireDate, EmployeeStatus.INACTIVE, createdAt, Instant.now(), version);
	}

	public UUID id() {
		return id;
	}

	public String fullName() {
		return fullName;
	}

	public String email() {
		return email;
	}

	public BigDecimal salary() {
		return salary;
	}

	public LocalDate hireDate() {
		return hireDate;
	}

	public EmployeeStatus status() {
		return status;
	}

	public Instant createdAt() {
		return createdAt;
	}

	public Instant updatedAt() {
		return updatedAt;
	}

	public long version() {
		return version;
	}

	private static UUID requireId(UUID id) {
		if (id == null) {
			throw new IllegalArgumentException("id is required");
		}
		return id;
	}

	private static LocalDate requireHireDate(LocalDate hireDate) {
		if (hireDate == null) {
			throw new IllegalArgumentException("hireDate is required");
		}
		return hireDate;
	}

	private static EmployeeStatus requireStatus(EmployeeStatus status) {
		if (status == null) {
			throw new IllegalArgumentException("status is required");
		}
		return status;
	}

	private static Instant requireInstant(Instant value, String fieldName) {
		if (value == null) {
			throw new IllegalArgumentException(fieldName + " is required");
		}
		return value;
	}

	private static String normalizeFullName(String fullName) {
		if (fullName == null || fullName.isBlank()) {
			throw new IllegalArgumentException("fullName is required");
		}
		String normalized = fullName.trim();
		if (normalized.length() > 120) {
			throw new IllegalArgumentException("fullName must be at most 120 characters");
		}
		return normalized;
	}

	private static String normalizeEmail(String email) {
		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("email is required");
		}
		String normalized = email.trim().toLowerCase(Locale.ROOT);
		if (!EMAIL_PATTERN.matcher(normalized).matches()) {
			throw new IllegalArgumentException("email has invalid format");
		}
		return normalized;
	}

	private static BigDecimal normalizeSalary(BigDecimal salary) {
		if (salary == null) {
			throw new IllegalArgumentException("salary is required");
		}
		if (salary.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("salary must be greater than zero");
		}
		return salary.setScale(2, RoundingMode.HALF_UP);
	}
}
