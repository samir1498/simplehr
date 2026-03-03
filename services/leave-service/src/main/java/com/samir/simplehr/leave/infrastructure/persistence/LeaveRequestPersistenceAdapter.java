package com.samir.simplehr.leave.infrastructure.persistence;

import com.samir.simplehr.leave.application.port.out.LeaveRequestRepositoryPort;
import com.samir.simplehr.leave.domain.model.LeaveRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class LeaveRequestPersistenceAdapter implements LeaveRequestRepositoryPort {

	private final SpringDataLeaveRequestRepository leaveRequestRepository;

	public LeaveRequestPersistenceAdapter(SpringDataLeaveRequestRepository leaveRequestRepository) {
		this.leaveRequestRepository = leaveRequestRepository;
	}

	@Override
	public Optional<LeaveRequest> findById(UUID leaveRequestId) {
		return leaveRequestRepository.findById(leaveRequestId).map(this::toDomain);
	}

	@Override
	public LeaveRequest save(LeaveRequest leaveRequest) {
		LeaveRequestJpaEntity entity = leaveRequestRepository.findById(leaveRequest.id()).orElseGet(LeaveRequestJpaEntity::new);
		boolean isNew = entity.getId() == null;

		entity.setId(leaveRequest.id());
		entity.setEmployeeId(leaveRequest.employeeId());
		entity.setStartDate(leaveRequest.startDate());
		entity.setEndDate(leaveRequest.endDate());
		entity.setStatus(leaveRequest.status());
		if (isNew) {
			entity.setRequestedAt(leaveRequest.requestedAt());
		}
		entity.setUpdatedAt(leaveRequest.updatedAt());
		entity.setReviewedAt(leaveRequest.reviewedAt());
		entity.setReviewedBy(leaveRequest.reviewedBy());
		entity.setRejectionReason(leaveRequest.rejectionReason());

		LeaveRequestJpaEntity saved = leaveRequestRepository.save(entity);
		return toDomain(saved);
	}

	@Override
	public boolean existsApprovedOverlap(UUID employeeId, LocalDate startDate, LocalDate endDate) {
		return leaveRequestRepository.existsApprovedOverlap(employeeId, startDate, endDate);
	}

	@Override
	public List<LeaveRequest> findApprovedByEmployeeAndYear(UUID employeeId, LocalDate yearStart, LocalDate yearEnd) {
		return leaveRequestRepository.findApprovedByEmployeeAndYear(employeeId, yearStart, yearEnd)
				.stream()
				.map(this::toDomain)
				.toList();
	}

	private LeaveRequest toDomain(LeaveRequestJpaEntity entity) {
		return LeaveRequest.rehydrate(
				entity.getId(),
				entity.getEmployeeId(),
				entity.getStartDate(),
				entity.getEndDate(),
				entity.getStatus(),
				entity.getRequestedAt(),
				entity.getUpdatedAt(),
				entity.getReviewedAt(),
				entity.getReviewedBy(),
				entity.getRejectionReason(),
				entity.getVersion()
		);
	}
}
