package com.samir.simplehr.leave.application.port.in;

import com.samir.simplehr.leave.application.command.ReviewLeaveCommand;
import com.samir.simplehr.leave.application.model.LeaveRequestResult;

import java.util.UUID;

public interface ApproveLeaveUseCase {
	LeaveRequestResult approveLeave(UUID leaveRequestId, ReviewLeaveCommand command);
}
