package com.samir.simplehr.leave.application.model;

import com.samir.simplehr.leave.domain.model.LeaveRequest;
import com.samir.simplehr.leave.domain.model.LeaveStatus;
import java.time.LocalDate;
import java.util.UUID;

public record LeaveRequestResult(
    UUID id,
    UUID employeeId,
    LocalDate startDate,
    LocalDate endDate,
    LeaveStatus status,
    String reviewedBy,
    String rejectionReason
) {
    public static LeaveRequestResult from(LeaveRequest leaveRequest) {
        return new LeaveRequestResult(
            leaveRequest.getId(),
            leaveRequest.getEmployeeId(),
            leaveRequest.getStartDate(),
            leaveRequest.getEndDate(),
            leaveRequest.getStatus(),
            leaveRequest.getReviewedBy(),
            leaveRequest.getRejectionReason()
        );
    }
}
