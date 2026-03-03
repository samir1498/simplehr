package com.samir.simplehr.employee.application.port.out;

import com.samir.simplehr.employee.domain.model.Employee;

public interface EmployeeEventPublisherPort {
	void publishEmployeeCreated(Employee employee);

	void publishEmployeeActivated(Employee employee);

	void publishEmployeeDeactivated(Employee employee);
}
