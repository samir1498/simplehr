package com.samir.simplehr.payroll.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataPayrollAdjustmentRepository extends JpaRepository<PayrollAdjustmentJpaEntity, UUID> {
}
