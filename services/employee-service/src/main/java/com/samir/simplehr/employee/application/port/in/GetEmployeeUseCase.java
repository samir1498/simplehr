package com.samir.simplehr.employee.application.port.in;

import com.samir.simplehr.employee.application.model.EmployeeResult;

import java.util.UUID;

public interface GetEmployeeUseCase {
	EmployeeResult getById(UUID employeeId);
}
