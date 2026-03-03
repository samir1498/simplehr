package com.samir.simplehr.payroll.application.port.in;

import com.samir.simplehr.payroll.application.model.LeaveApprovedEvent;

public interface HandleLeaveApprovedUseCase {
	void handle(LeaveApprovedEvent event);
}
