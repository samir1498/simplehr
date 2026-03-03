package com.samir.simplehr.leave.application.port.in;

import com.samir.simplehr.leave.application.model.LeaveRequestResult;

import java.util.UUID;

public interface GetLeaveRequestUseCase {
	LeaveRequestResult getById(UUID leaveRequestId);
}
