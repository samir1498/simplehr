package com.samir.simplehr.employee.application.port.in;

import com.samir.simplehr.employee.application.command.CreateEmployeeCommand;
import com.samir.simplehr.employee.application.model.EmployeeResult;

public interface CreateEmployeeUseCase {
	EmployeeResult create(CreateEmployeeCommand command);
}
