package com.samir.simplehr.employee.adapters.out.persistence;

import com.samir.simplehr.employee.application.port.out.EmployeeRepositoryPort;
import com.samir.simplehr.employee.domain.model.Employee;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class EmployeePersistenceAdapter implements EmployeeRepositoryPort {

    private final SpringDataEmployeeRepository employeeRepository;

    public EmployeePersistenceAdapter(
        SpringDataEmployeeRepository employeeRepository
    ) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Optional<Employee> findById(UUID employeeId) {
        return employeeRepository.findById(employeeId).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(
            email.trim().toLowerCase(Locale.ROOT)
        );
    }

    @Override
    public Employee save(Employee employee) {
        EmployeeJpaEntity entity = employeeRepository
            .findById(employee.getId())
            .orElseGet(EmployeeJpaEntity::new);
        boolean isNew = entity.getId() == null;

        entity.setId(employee.getId());
        entity.setFullName(employee.getFullName());
        entity.setEmail(employee.getEmail());
        entity.setSalary(employee.getSalary());
        entity.setHireDate(employee.getHireDate());
        entity.setStatus(employee.getStatus());
        if (isNew) {
            entity.setCreatedAt(employee.getCreatedAt());
        }
        entity.setUpdatedAt(employee.getUpdatedAt());

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
