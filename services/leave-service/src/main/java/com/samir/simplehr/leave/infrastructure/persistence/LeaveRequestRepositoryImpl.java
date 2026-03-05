package com.samir.simplehr.leave.infrastructure.persistence;

import com.samir.simplehr.leave.application.gateway.LeaveRequestRepository;
import com.samir.simplehr.leave.domain.model.LeaveRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class LeaveRequestRepositoryImpl implements LeaveRequestRepository {

    private final SpringDataLeaveRequestRepository leaveRequestRepository;

    public LeaveRequestRepositoryImpl(
        SpringDataLeaveRequestRepository leaveRequestRepository
    ) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    @Override
    public Optional<LeaveRequest> findById(UUID leaveRequestId) {
        return leaveRequestRepository
            .findById(leaveRequestId)
            .map(this::toDomain);
    }

    @Override
    public LeaveRequest save(LeaveRequest leaveRequest) {
        LeaveRequestJpaEntity entity = leaveRequestRepository
            .findById(leaveRequest.getId())
            .orElseGet(LeaveRequestJpaEntity::new);
        boolean isNew = entity.getId() == null;

        entity.setId(leaveRequest.getId());
        entity.setEmployeeId(leaveRequest.getEmployeeId());
        entity.setStartDate(leaveRequest.getStartDate());
        entity.setEndDate(leaveRequest.getEndDate());
        entity.setStatus(leaveRequest.getStatus());
        if (isNew) {
            entity.setRequestedAt(leaveRequest.getRequestedAt());
        }
        entity.setUpdatedAt(leaveRequest.getUpdatedAt());
        entity.setReviewedAt(leaveRequest.getReviewedAt());
        entity.setReviewedBy(leaveRequest.getReviewedBy());
        entity.setRejectionReason(leaveRequest.getRejectionReason());

        LeaveRequestJpaEntity saved = leaveRequestRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public boolean existsApprovedOverlap(
        UUID employeeId,
        LocalDate startDate,
        LocalDate endDate
    ) {
        return leaveRequestRepository.existsApprovedOverlap(
            employeeId,
            startDate,
            endDate
        );
    }

    @Override
    public List<LeaveRequest> findApprovedByEmployeeAndYear(
        UUID employeeId,
        LocalDate yearStart,
        LocalDate yearEnd
    ) {
        return leaveRequestRepository
            .findApprovedByEmployeeAndYear(employeeId, yearStart, yearEnd)
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
