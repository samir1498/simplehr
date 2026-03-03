package com.samir.simplehr.leave.application.port.in;

import com.samir.simplehr.leave.application.command.RequestLeaveCommand;
import com.samir.simplehr.leave.application.model.LeaveRequestResult;

public interface RequestLeaveUseCase {
	LeaveRequestResult requestLeave(RequestLeaveCommand command);
}
