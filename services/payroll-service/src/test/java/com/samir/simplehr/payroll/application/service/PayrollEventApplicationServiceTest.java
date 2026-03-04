package com.samir.simplehr.payroll.application.service;

import com.samir.simplehr.payroll.application.model.EmployeeCreatedEvent;
import com.samir.simplehr.payroll.application.model.LeaveApprovedEvent;
import com.samir.simplehr.payroll.application.port.out.PayrollAdjustmentRepositoryPort;
import com.samir.simplehr.payroll.application.port.out.PayrollProfileRepositoryPort;
import com.samir.simplehr.payroll.application.port.out.ProcessedEventRepositoryPort;
import com.samir.simplehr.payroll.domain.model.PayrollAdjustment;
import com.samir.simplehr.payroll.domain.model.PayrollProfile;
import com.samir.simplehr.payroll.domain.model.ProcessedEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PayrollEventApplicationServiceTest {

	@Test
	void shouldSkipDuplicateEmployeeCreatedEvent() {
		PayrollProfileRepositoryPort profileRepository = mock(PayrollProfileRepositoryPort.class);
		PayrollAdjustmentRepositoryPort adjustmentRepository = mock(PayrollAdjustmentRepositoryPort.class);
		ProcessedEventRepositoryPort processedEventRepository = mock(ProcessedEventRepositoryPort.class);
		PayrollEventApplicationService service = new PayrollEventApplicationService(profileRepository, adjustmentRepository, processedEventRepository);

		UUID eventId = UUID.randomUUID();
		when(processedEventRepository.existsByEventId(eventId)).thenReturn(true);

		service.handle(new EmployeeCreatedEvent(eventId, UUID.randomUUID(), new BigDecimal("200000")));

		verify(profileRepository, never()).save(any());
		verify(processedEventRepository, never()).save(any());
	}

	@Test
	void shouldCreatePayrollProfileOnEmployeeCreated() {
		PayrollProfileRepositoryPort profileRepository = mock(PayrollProfileRepositoryPort.class);
		PayrollAdjustmentRepositoryPort adjustmentRepository = mock(PayrollAdjustmentRepositoryPort.class);
		ProcessedEventRepositoryPort processedEventRepository = mock(ProcessedEventRepositoryPort.class);
		PayrollEventApplicationService service = new PayrollEventApplicationService(profileRepository, adjustmentRepository, processedEventRepository);

		UUID eventId = UUID.randomUUID();
		UUID employeeId = UUID.randomUUID();
		when(processedEventRepository.existsByEventId(eventId)).thenReturn(false);
		when(profileRepository.findByEmployeeId(employeeId)).thenReturn(Optional.empty());

		service.handle(new EmployeeCreatedEvent(eventId, employeeId, new BigDecimal("150000")));

		ArgumentCaptor<PayrollProfile> profileCaptor = ArgumentCaptor.forClass(PayrollProfile.class);
		verify(profileRepository).save(profileCaptor.capture());
		assertEquals(employeeId, profileCaptor.getValue().employeeId());
		assertEquals(new BigDecimal("150000.00"), profileCaptor.getValue().baseSalary());

		verify(processedEventRepository).save(any(ProcessedEvent.class));
	}

	@Test
	void shouldCreateDeductionOnLeaveApproved() {
		PayrollProfileRepositoryPort profileRepository = mock(PayrollProfileRepositoryPort.class);
		PayrollAdjustmentRepositoryPort adjustmentRepository = mock(PayrollAdjustmentRepositoryPort.class);
		ProcessedEventRepositoryPort processedEventRepository = mock(ProcessedEventRepositoryPort.class);
		PayrollEventApplicationService service = new PayrollEventApplicationService(profileRepository, adjustmentRepository, processedEventRepository);

		UUID eventId = UUID.randomUUID();
		UUID employeeId = UUID.randomUUID();
		UUID leaveRequestId = UUID.randomUUID();

		when(processedEventRepository.existsByEventId(eventId)).thenReturn(false);
		when(profileRepository.findByEmployeeId(employeeId)).thenReturn(Optional.of(
				PayrollProfile.create(employeeId, new BigDecimal("300000"))
		));

		service.handle(new LeaveApprovedEvent(
				eventId,
				employeeId,
				leaveRequestId,
				LocalDate.of(2026, 7, 1),
				LocalDate.of(2026, 7, 3)
		));

		ArgumentCaptor<PayrollAdjustment> adjustmentCaptor = ArgumentCaptor.forClass(PayrollAdjustment.class);
		verify(adjustmentRepository).save(adjustmentCaptor.capture());
		assertEquals(leaveRequestId, adjustmentCaptor.getValue().leaveRequestId());
		assertEquals(new BigDecimal("30000.00"), adjustmentCaptor.getValue().amount());
		verify(processedEventRepository).save(any(ProcessedEvent.class));
	}
}
