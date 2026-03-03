package com.samir.simplehr.payroll.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataPayrollProfileRepository extends JpaRepository<PayrollProfileJpaEntity, UUID> {
	Optional<PayrollProfileJpaEntity> findByEmployeeId(UUID employeeId);
}
