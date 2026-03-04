package com.samir.simplehr.payroll.application.port.out;

import com.samir.simplehr.payroll.domain.model.PayrollProfile;

import java.util.Optional;
import java.util.UUID;

public interface PayrollProfileRepositoryPort {
	Optional<PayrollProfile> findByEmployeeId(UUID employeeId);

	PayrollProfile save(PayrollProfile profile);
}
