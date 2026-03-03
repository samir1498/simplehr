package com.samir.simplehr.payroll.adapters.out.persistence;

import com.samir.simplehr.payroll.application.port.out.PayrollAdjustmentRepositoryPort;
import com.samir.simplehr.payroll.domain.model.PayrollAdjustment;
import org.springframework.stereotype.Component;

@Component
public class PayrollAdjustmentPersistenceAdapter implements PayrollAdjustmentRepositoryPort {

	private final SpringDataPayrollAdjustmentRepository payrollAdjustmentRepository;

	public PayrollAdjustmentPersistenceAdapter(SpringDataPayrollAdjustmentRepository payrollAdjustmentRepository) {
		this.payrollAdjustmentRepository = payrollAdjustmentRepository;
	}

	@Override
	public PayrollAdjustment save(PayrollAdjustment adjustment) {
		PayrollAdjustmentJpaEntity entity = new PayrollAdjustmentJpaEntity();
		entity.setId(adjustment.id());
		entity.setEmployeeId(adjustment.employeeId());
		entity.setLeaveRequestId(adjustment.leaveRequestId());
		entity.setAmount(adjustment.amount());
		entity.setReason(adjustment.reason());
		entity.setCreatedAt(adjustment.createdAt());
		payrollAdjustmentRepository.save(entity);
		return adjustment;
	}
}
