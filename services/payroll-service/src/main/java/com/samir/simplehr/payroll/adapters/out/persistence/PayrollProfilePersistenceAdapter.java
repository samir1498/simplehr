package com.samir.simplehr.payroll.adapters.out.persistence;

import com.samir.simplehr.payroll.application.port.out.PayrollProfileRepositoryPort;
import com.samir.simplehr.payroll.domain.model.PayrollProfile;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PayrollProfilePersistenceAdapter implements PayrollProfileRepositoryPort {

	private final SpringDataPayrollProfileRepository payrollProfileRepository;

	public PayrollProfilePersistenceAdapter(SpringDataPayrollProfileRepository payrollProfileRepository) {
		this.payrollProfileRepository = payrollProfileRepository;
	}

	@Override
	public Optional<PayrollProfile> findByEmployeeId(UUID employeeId) {
		return payrollProfileRepository.findByEmployeeId(employeeId).map(this::toDomain);
	}

	@Override
	public PayrollProfile save(PayrollProfile profile) {
		PayrollProfileJpaEntity entity = payrollProfileRepository.findByEmployeeId(profile.employeeId()).orElseGet(PayrollProfileJpaEntity::new);
		boolean isNew = entity.getId() == null;

		entity.setId(profile.id());
		entity.setEmployeeId(profile.employeeId());
		entity.setBaseSalary(profile.baseSalary());
		entity.setActive(profile.active());
		if (isNew) {
			entity.setCreatedAt(profile.createdAt());
		}
		entity.setUpdatedAt(profile.updatedAt());

		return toDomain(payrollProfileRepository.save(entity));
	}

	private PayrollProfile toDomain(PayrollProfileJpaEntity entity) {
		return PayrollProfile.rehydrate(
				entity.getId(),
				entity.getEmployeeId(),
				entity.getBaseSalary(),
				entity.isActive(),
				entity.getCreatedAt(),
				entity.getUpdatedAt(),
				entity.getVersion()
		);
	}
}
