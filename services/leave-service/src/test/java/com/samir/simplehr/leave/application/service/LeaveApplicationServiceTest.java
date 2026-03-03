package com.samir.simplehr.leave.application.service;

import com.samir.simplehr.leave.application.command.RequestLeaveCommand;
import com.samir.simplehr.leave.application.command.ReviewLeaveCommand;
import com.samir.simplehr.leave.application.exception.BusinessRuleViolationException;
import com.samir.simplehr.leave.application.exception.UnauthorizedOperationException;
import com.samir.simplehr.leave.application.port.out.LeaveRequestRepositoryPort;
import com.samir.simplehr.leave.domain.model.LeaveRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LeaveApplicationServiceTest {

	@Test
	void shouldRejectOverlapWithApprovedLeave() {
		LeaveRequestRepositoryPort repository = mock(LeaveRequestRepositoryPort.class);
		LeaveApplicationService service = new LeaveApplicationService(repository);

		when(repository.existsApprovedOverlap(any(), any(), any())).thenReturn(true);

		assertThrows(BusinessRuleViolationException.class, () -> service.requestLeave(new RequestLeaveCommand(
				UUID.randomUUID(),
				LocalDate.of(2026, 8, 1),
				LocalDate.of(2026, 8, 3)
		)));
	}

	@Test
	void shouldRejectWhenYearlyAllowanceExceeded() {
		LeaveRequestRepositoryPort repository = mock(LeaveRequestRepositoryPort.class);
		LeaveApplicationService service = new LeaveApplicationService(repository);
		UUID employeeId = UUID.randomUUID();

		when(repository.existsApprovedOverlap(any(), any(), any())).thenReturn(false);
		when(repository.findApprovedByEmployeeAndYear(any(), any(), any())).thenReturn(List.of(
				LeaveRequest.rehydrate(
						UUID.randomUUID(),
						employeeId,
						LocalDate.of(2026, 1, 1),
						LocalDate.of(2026, 1, 29),
						com.samir.simplehr.leave.domain.model.LeaveStatus.APPROVED,
						java.time.Instant.now(),
						java.time.Instant.now(),
						java.time.Instant.now(),
						"manager-1",
						null,
						0
				)
		));

		assertThrows(BusinessRuleViolationException.class, () -> service.requestLeave(new RequestLeaveCommand(
				employeeId,
				LocalDate.of(2026, 2, 1),
				LocalDate.of(2026, 2, 5)
		)));
	}

	@Test
	void shouldOnlyAllowManagerToApprove() {
		LeaveRequestRepositoryPort repository = mock(LeaveRequestRepositoryPort.class);
		LeaveApplicationService service = new LeaveApplicationService(repository);
		UUID leaveId = UUID.randomUUID();

		when(repository.findById(leaveId)).thenReturn(Optional.of(LeaveRequest.request(
				UUID.randomUUID(),
				LocalDate.of(2026, 7, 1),
				LocalDate.of(2026, 7, 2)
		)));

		assertThrows(UnauthorizedOperationException.class, () -> service.approveLeave(
				leaveId,
				new ReviewLeaveCommand("EMPLOYEE", "u-1", null)
		));
	}
}
