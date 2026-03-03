package com.samir.simplehr.employee.application.port.out;

import com.samir.simplehr.employee.domain.model.Employee;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepositoryPort {
	Optional<Employee> findById(UUID employeeId);

	boolean existsByEmail(String email);

	Employee save(Employee employee);
}
