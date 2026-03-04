package com.samir.simplehr.payroll.adapters.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payroll_adjustments")
public class PayrollAdjustmentJpaEntity {
	@Id
	@Column(nullable = false, updatable = false)
	private UUID id;

	@Column(name = "employee_id", nullable = false)
	private UUID employeeId;

	@Column(name = "leave_request_id", nullable = false)
	private UUID leaveRequestId;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal amount;

	@Column(nullable = false, length = 50)
	private String reason;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	public UUID getId() { return id; }
	public void setId(UUID id) { this.id = id; }
	public UUID getEmployeeId() { return employeeId; }
	public void setEmployeeId(UUID employeeId) { this.employeeId = employeeId; }
	public UUID getLeaveRequestId() { return leaveRequestId; }
	public void setLeaveRequestId(UUID leaveRequestId) { this.leaveRequestId = leaveRequestId; }
	public BigDecimal getAmount() { return amount; }
	public void setAmount(BigDecimal amount) { this.amount = amount; }
	public String getReason() { return reason; }
	public void setReason(String reason) { this.reason = reason; }
	public Instant getCreatedAt() { return createdAt; }
	public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
