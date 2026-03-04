package com.samir.simplehr.payroll.adapters.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payroll_profiles")
@Getter
@Setter
@NoArgsConstructor
public class PayrollProfileJpaEntity {
	@Id
	@Column(nullable = false, updatable = false)
	private UUID id;

	@Column(name = "employee_id", nullable = false, unique = true)
	private UUID employeeId;

	@Column(name = "base_salary", nullable = false, precision = 12, scale = 2)
	private BigDecimal baseSalary;

	@Column(nullable = false)
	private boolean active;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	@Version
	@Column(nullable = false)
	private long version;
}
