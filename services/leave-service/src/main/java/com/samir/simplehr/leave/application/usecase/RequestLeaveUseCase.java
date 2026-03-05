package com.samir.simplehr.leave.application.usecase;

import com.samir.simplehr.leave.application.command.RequestLeaveCommand;
import com.samir.simplehr.leave.application.model.LeaveRequestResult;

public interface RequestLeaveUseCase {
	LeaveRequestResult requestLeave(RequestLeaveCommand command);
}
