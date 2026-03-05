package com.samir.simplehr.leave.application.service;

import com.samir.simplehr.leave.application.command.RequestLeaveCommand;
import com.samir.simplehr.leave.application.command.ReviewLeaveCommand;
import com.samir.simplehr.leave.application.exception.BusinessRuleViolationException;
import com.samir.simplehr.leave.application.exception.ResourceNotFoundException;
import com.samir.simplehr.leave.application.exception.UnauthorizedOperationException;
import com.samir.simplehr.leave.application.gateway.LeaveRequestRepository;
import com.samir.simplehr.leave.application.model.LeaveRequestResult;
import com.samir.simplehr.leave.application.usecase.ApproveLeaveUseCase;
import com.samir.simplehr.leave.application.usecase.GetLeaveRequestUseCase;
import com.samir.simplehr.leave.application.usecase.RejectLeaveUseCase;
import com.samir.simplehr.leave.application.usecase.RequestLeaveUseCase;
import com.samir.simplehr.leave.domain.model.LeaveRequest;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LeaveApplicationService
    implements
        RequestLeaveUseCase,
        ApproveLeaveUseCase,
        RejectLeaveUseCase,
        GetLeaveRequestUseCase
{

    private static final long YEARLY_ALLOWANCE_DAYS = 30;

    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveApplicationService(
        LeaveRequestRepository leaveRequestRepository
    ) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    @Override
    public LeaveRequestResult requestLeave(RequestLeaveCommand command) {
        LeaveRequest leaveRequest = LeaveRequest.request(
            command.employeeId(),
            command.startDate(),
            command.endDate()
        );

        if (
            leaveRequestRepository.existsApprovedOverlap(
                leaveRequest.getEmployeeId(),
                leaveRequest.getStartDate(),
                leaveRequest.getEndDate()
            )
        ) {
            throw new BusinessRuleViolationException(
                "leave request overlaps existing approved leave"
            );
        }

        LocalDate yearStart = LocalDate.of(
            leaveRequest.getStartDate().getYear(),
            1,
            1
        );
        LocalDate yearEnd = LocalDate.of(
            leaveRequest.getStartDate().getYear(),
            12,
            31
        );
        long alreadyApprovedDays = leaveRequestRepository
            .findApprovedByEmployeeAndYear(
                leaveRequest.getEmployeeId(),
                yearStart,
                yearEnd
            )
            .stream()
            .mapToLong(LeaveRequest::daysRequested)
            .sum();

        if (
            alreadyApprovedDays + leaveRequest.daysRequested() >
            YEARLY_ALLOWANCE_DAYS
        ) {
            throw new BusinessRuleViolationException(
                "yearly leave allowance exceeded"
            );
        }

        return LeaveRequestResult.from(
            leaveRequestRepository.save(leaveRequest)
        );
    }

    @Override
    public LeaveRequestResult approveLeave(
        UUID leaveRequestId,
        ReviewLeaveCommand command
    ) {
        requireManagerRole(command.role());
        LeaveRequest leaveRequest = loadLeaveRequest(leaveRequestId);
        return LeaveRequestResult.from(
            leaveRequestRepository.save(
                leaveRequest.approve(command.reviewerId())
            )
        );
    }

    @Override
    public LeaveRequestResult rejectLeave(
        UUID leaveRequestId,
        ReviewLeaveCommand command
    ) {
        requireManagerRole(command.role());
        LeaveRequest leaveRequest = loadLeaveRequest(leaveRequestId);
        return LeaveRequestResult.from(
            leaveRequestRepository.save(
                leaveRequest.reject(
                    command.reviewerId(),
                    command.rejectionReason()
                )
            )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public LeaveRequestResult getById(UUID leaveRequestId) {
        return LeaveRequestResult.from(loadLeaveRequest(leaveRequestId));
    }

    private LeaveRequest loadLeaveRequest(UUID leaveRequestId) {
        return leaveRequestRepository
            .findById(leaveRequestId)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "leave request not found: " + leaveRequestId
                )
            );
    }

    private static void requireManagerRole(String role) {
        if (role == null || !"MANAGER".equalsIgnoreCase(role.trim())) {
            throw new UnauthorizedOperationException(
                "only MANAGER can approve or reject leaves"
            );
        }
    }
}
