package com.samir.simplehr.payroll.adapters.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payroll_profiles")
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

	public UUID getId() { return id; }
	public void setId(UUID id) { this.id = id; }
	public UUID getEmployeeId() { return employeeId; }
	public void setEmployeeId(UUID employeeId) { this.employeeId = employeeId; }
	public BigDecimal getBaseSalary() { return baseSalary; }
	public void setBaseSalary(BigDecimal baseSalary) { this.baseSalary = baseSalary; }
	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }
	public Instant getCreatedAt() { return createdAt; }
	public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
	public Instant getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
	public long getVersion() { return version; }
	public void setVersion(long version) { this.version = version; }
}
