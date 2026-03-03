package com.samir.simplehr.leave.application.port.out;

import com.samir.simplehr.leave.domain.model.LeaveRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeaveRequestRepositoryPort {
	Optional<LeaveRequest> findById(UUID leaveRequestId);

	LeaveRequest save(LeaveRequest leaveRequest);

	boolean existsApprovedOverlap(UUID employeeId, LocalDate startDate, LocalDate endDate);

	List<LeaveRequest> findApprovedByEmployeeAndYear(UUID employeeId, LocalDate yearStart, LocalDate yearEnd);
}
