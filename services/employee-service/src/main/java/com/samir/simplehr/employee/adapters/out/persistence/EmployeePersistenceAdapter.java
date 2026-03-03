package com.samir.simplehr.employee.adapters.out.persistence;

import com.samir.simplehr.employee.application.port.out.EmployeeRepositoryPort;
import com.samir.simplehr.employee.domain.model.Employee;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class EmployeePersistenceAdapter implements EmployeeRepositoryPort {

	private final SpringDataEmployeeRepository employeeRepository;

	public EmployeePersistenceAdapter(SpringDataEmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public Optional<Employee> findById(UUID employeeId) {
		return employeeRepository.findById(employeeId).map(this::toDomain);
	}

	@Override
	public boolean existsByEmail(String email) {
		return employeeRepository.existsByEmail(email.trim().toLowerCase());
	}

	@Override
	public Employee save(Employee employee) {
		EmployeeJpaEntity entity = employeeRepository.findById(employee.id()).orElseGet(EmployeeJpaEntity::new);
		boolean isNew = entity.getId() == null;

		entity.setId(employee.id());
		entity.setFullName(employee.fullName());
		entity.setEmail(employee.email());
		entity.setSalary(employee.salary());
		entity.setHireDate(employee.hireDate());
		entity.setStatus(employee.status());
		if (isNew) {
			entity.setCreatedAt(employee.createdAt());
		}
		entity.setUpdatedAt(employee.updatedAt());

		EmployeeJpaEntity savedEntity = employeeRepository.save(entity);
		return toDomain(savedEntity);
	}

	private Employee toDomain(EmployeeJpaEntity entity) {
		return Employee.rehydrate(
				entity.getId(),
				entity.getFullName(),
				entity.getEmail(),
				entity.getSalary(),
				entity.getHireDate(),
				entity.getStatus(),
				entity.getCreatedAt(),
				entity.getUpdatedAt(),
				entity.getVersion()
		);
	}
}
