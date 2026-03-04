package com.samir.simplehr.payroll.application.service;

import com.samir.simplehr.payroll.application.model.EmployeeCreatedEvent;
import com.samir.simplehr.payroll.application.model.LeaveApprovedEvent;
import com.samir.simplehr.payroll.application.port.in.HandleEmployeeCreatedUseCase;
import com.samir.simplehr.payroll.application.port.in.HandleLeaveApprovedUseCase;
import com.samir.simplehr.payroll.application.port.out.PayrollAdjustmentRepositoryPort;
import com.samir.simplehr.payroll.application.port.out.PayrollProfileRepositoryPort;
import com.samir.simplehr.payroll.application.port.out.ProcessedEventRepositoryPort;
import com.samir.simplehr.payroll.domain.model.PayrollAdjustment;
import com.samir.simplehr.payroll.domain.model.PayrollProfile;
import com.samir.simplehr.payroll.domain.model.ProcessedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
public class PayrollEventApplicationService implements HandleEmployeeCreatedUseCase, HandleLeaveApprovedUseCase {

	private final PayrollProfileRepositoryPort payrollProfileRepository;
	private final PayrollAdjustmentRepositoryPort payrollAdjustmentRepository;
	private final ProcessedEventRepositoryPort processedEventRepository;

	public PayrollEventApplicationService(
			PayrollProfileRepositoryPort payrollProfileRepository,
			PayrollAdjustmentRepositoryPort payrollAdjustmentRepository,
			ProcessedEventRepositoryPort processedEventRepository
	) {
		this.payrollProfileRepository = payrollProfileRepository;
		this.payrollAdjustmentRepository = payrollAdjustmentRepository;
		this.processedEventRepository = processedEventRepository;
	}

	@Override
	public void handle(EmployeeCreatedEvent event) {
		if (processedEventRepository.existsByEventId(event.eventId())) {
			return;
		}

		PayrollProfile profile = payrollProfileRepository.findByEmployeeId(event.employeeId())
				.map(existing -> existing.updateBaseSalary(event.salary()))
				.orElseGet(() -> PayrollProfile.create(event.employeeId(), event.salary()));

		payrollProfileRepository.save(profile);
		processedEventRepository.save(ProcessedEvent.from(event.eventId(), "EmployeeCreated"));
	}

	@Override
	public void handle(LeaveApprovedEvent event) {
		if (processedEventRepository.existsByEventId(event.eventId())) {
			return;
		}

		PayrollProfile profile = payrollProfileRepository.findByEmployeeId(event.employeeId())
				.orElseThrow(() -> new IllegalArgumentException("missing payroll profile for employee: " + event.employeeId()));

		long leaveDays = ChronoUnit.DAYS.between(event.startDate(), event.endDate()) + 1;
		if (leaveDays < 1) {
			throw new IllegalArgumentException("invalid leave period");
		}

		BigDecimal dailyRate = profile.baseSalary().divide(BigDecimal.valueOf(30), 2, RoundingMode.HALF_UP);
		BigDecimal deduction = dailyRate.multiply(BigDecimal.valueOf(leaveDays)).setScale(2, RoundingMode.HALF_UP);

		payrollAdjustmentRepository.save(PayrollAdjustment.leaveDeduction(event.employeeId(), event.leaveRequestId(), deduction));
		processedEventRepository.save(ProcessedEvent.from(event.eventId(), "LeaveApproved"));
	}
}
