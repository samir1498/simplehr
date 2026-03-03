package com.samir.simplehr.leave.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public final class LeaveRequest {
	private final UUID id;
	private final UUID employeeId;
	private final LocalDate startDate;
	private final LocalDate endDate;
	private final LeaveStatus status;
	private final Instant requestedAt;
	private final Instant updatedAt;
	private final Instant reviewedAt;
	private final String reviewedBy;
	private final String rejectionReason;
	private final long version;

	private LeaveRequest(
			UUID id,
			UUID employeeId,
			LocalDate startDate,
			LocalDate endDate,
			LeaveStatus status,
			Instant requestedAt,
			Instant updatedAt,
			Instant reviewedAt,
			String reviewedBy,
			String rejectionReason,
			long version
	) {
		this.id = requireId(id);
		this.employeeId = requireEmployeeId(employeeId);
		this.startDate = requireStartDate(startDate);
		this.endDate = requireEndDate(endDate, startDate);
		this.status = requireStatus(status);
		this.requestedAt = requireInstant(requestedAt, "requestedAt");
		this.updatedAt = requireInstant(updatedAt, "updatedAt");
		this.reviewedAt = reviewedAt;
		this.reviewedBy = normalizeOptional(reviewedBy);
		this.rejectionReason = normalizeOptional(rejectionReason);
		this.version = version;
	}

	public static LeaveRequest request(UUID employeeId, LocalDate startDate, LocalDate endDate) {
		Instant now = Instant.now();
		return new LeaveRequest(
				UUID.randomUUID(),
				employeeId,
				startDate,
				endDate,
				LeaveStatus.PENDING,
				now,
				now,
				null,
				null,
				null,
				0
		);
	}

	public static LeaveRequest rehydrate(
			UUID id,
			UUID employeeId,
			LocalDate startDate,
			LocalDate endDate,
			LeaveStatus status,
			Instant requestedAt,
			Instant updatedAt,
			Instant reviewedAt,
			String reviewedBy,
			String rejectionReason,
			long version
	) {
		return new LeaveRequest(id, employeeId, startDate, endDate, status, requestedAt, updatedAt, reviewedAt, reviewedBy, rejectionReason, version);
	}

	public LeaveRequest approve(String reviewerId) {
		String normalizedReviewer = requireReviewer(reviewerId);
		Instant now = Instant.now();
		if (status == LeaveStatus.APPROVED) {
			return this;
		}
		if (status == LeaveStatus.REJECTED) {
			throw new IllegalStateException("cannot approve a rejected leave request");
		}
		return new LeaveRequest(
				id,
				employeeId,
				startDate,
				endDate,
				LeaveStatus.APPROVED,
				requestedAt,
				now,
				now,
				normalizedReviewer,
				null,
				version
		);
	}

	public LeaveRequest reject(String reviewerId, String reason) {
		String normalizedReviewer = requireReviewer(reviewerId);
		String normalizedReason = requireReason(reason);
		Instant now = Instant.now();
		if (status == LeaveStatus.REJECTED) {
			return this;
		}
		if (status == LeaveStatus.APPROVED) {
			throw new IllegalStateException("cannot reject an approved leave request");
		}
		return new LeaveRequest(
				id,
				employeeId,
				startDate,
				endDate,
				LeaveStatus.REJECTED,
				requestedAt,
				now,
				now,
				normalizedReviewer,
				normalizedReason,
				version
		);
	}

	public long daysRequested() {
		return ChronoUnit.DAYS.between(startDate, endDate) + 1;
	}

	public UUID id() {
		return id;
	}

	public UUID employeeId() {
		return employeeId;
	}

	public LocalDate startDate() {
		return startDate;
	}

	public LocalDate endDate() {
		return endDate;
	}

	public LeaveStatus status() {
		return status;
	}

	public Instant requestedAt() {
		return requestedAt;
	}

	public Instant updatedAt() {
		return updatedAt;
	}

	public Instant reviewedAt() {
		return reviewedAt;
	}

	public String reviewedBy() {
		return reviewedBy;
	}

	public String rejectionReason() {
		return rejectionReason;
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

	private static UUID requireEmployeeId(UUID employeeId) {
		if (employeeId == null) {
			throw new IllegalArgumentException("employeeId is required");
		}
		return employeeId;
	}

	private static LeaveStatus requireStatus(LeaveStatus status) {
		if (status == null) {
			throw new IllegalArgumentException("status is required");
		}
		return status;
	}

	private static LocalDate requireStartDate(LocalDate startDate) {
		if (startDate == null) {
			throw new IllegalArgumentException("startDate is required");
		}
		return startDate;
	}

	private static LocalDate requireEndDate(LocalDate endDate, LocalDate startDate) {
		if (endDate == null) {
			throw new IllegalArgumentException("endDate is required");
		}
		if (startDate != null && endDate.isBefore(startDate)) {
			throw new IllegalArgumentException("endDate cannot be before startDate");
		}
		if (startDate != null && startDate.getYear() != endDate.getYear()) {
			throw new IllegalArgumentException("leave request must be within the same calendar year");
		}
		return endDate;
	}

	private static Instant requireInstant(Instant value, String field) {
		if (value == null) {
			throw new IllegalArgumentException(field + " is required");
		}
		return value;
	}

	private static String requireReviewer(String reviewerId) {
		String normalized = normalizeOptional(reviewerId);
		if (normalized == null) {
			throw new IllegalArgumentException("reviewerId is required");
		}
		return normalized;
	}

	private static String requireReason(String reason) {
		String normalized = normalizeOptional(reason);
		if (normalized == null) {
			throw new IllegalArgumentException("rejectionReason is required");
		}
		return normalized;
	}

	private static String normalizeOptional(String value) {
		if (value == null) {
			return null;
		}
		String normalized = value.trim();
		return normalized.isEmpty() ? null : normalized;
	}
}
