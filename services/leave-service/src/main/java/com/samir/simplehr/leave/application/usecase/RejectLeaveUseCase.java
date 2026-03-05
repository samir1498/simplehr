package com.samir.simplehr.leave.application.usecase;

import com.samir.simplehr.leave.application.command.ReviewLeaveCommand;
import com.samir.simplehr.leave.application.model.LeaveRequestResult;

import java.util.UUID;

public interface RejectLeaveUseCase {
	LeaveRequestResult rejectLeave(UUID leaveRequestId, ReviewLeaveCommand command);
}
