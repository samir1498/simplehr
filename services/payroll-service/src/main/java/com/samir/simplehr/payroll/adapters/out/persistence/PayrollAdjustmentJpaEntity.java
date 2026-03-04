package com.samir.simplehr.payroll.adapters.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payroll_adjustments")
@Getter
@Setter
@NoArgsConstructor
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
}
