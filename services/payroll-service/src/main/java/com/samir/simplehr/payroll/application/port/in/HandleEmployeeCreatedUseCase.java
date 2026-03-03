package com.samir.simplehr.payroll.application.port.in;

import com.samir.simplehr.payroll.application.model.EmployeeCreatedEvent;

public interface HandleEmployeeCreatedUseCase {
	void handle(EmployeeCreatedEvent event);
}
