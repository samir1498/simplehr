package com.samir.simplehr.employee.application.service;

import com.samir.simplehr.employee.application.command.CreateEmployeeCommand;
import com.samir.simplehr.employee.application.exception.DuplicateResourceException;
import com.samir.simplehr.employee.application.exception.ResourceNotFoundException;
import com.samir.simplehr.employee.application.model.EmployeeResult;
import com.samir.simplehr.employee.application.port.in.ActivateEmployeeUseCase;
import com.samir.simplehr.employee.application.port.in.CreateEmployeeUseCase;
import com.samir.simplehr.employee.application.port.in.DeactivateEmployeeUseCase;
import com.samir.simplehr.employee.application.port.in.GetEmployeeUseCase;
import com.samir.simplehr.employee.application.port.out.EmployeeEventPublisherPort;
import com.samir.simplehr.employee.application.port.out.EmployeeRepositoryPort;
import com.samir.simplehr.employee.domain.model.Employee;
import com.samir.simplehr.employee.domain.model.EmployeeStatus;
import java.time.Instant;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeApplicationService
    implements
        CreateEmployeeUseCase,
        ActivateEmployeeUseCase,
        DeactivateEmployeeUseCase,
        GetEmployeeUseCase
{

    private final EmployeeRepositoryPort employeeRepository;
    private final EmployeeEventPublisherPort employeeEventPublisher;

    public EmployeeApplicationService(
        EmployeeRepositoryPort employeeRepository,
        EmployeeEventPublisherPort employeeEventPublisher
    ) {
        this.employeeRepository = employeeRepository;
        this.employeeEventPublisher = employeeEventPublisher;
    }

    @Override
    public EmployeeResult create(CreateEmployeeCommand command) {
        if (employeeRepository.existsByEmail(command.email())) {
            throw new DuplicateResourceException(
                "employee already exists with email: " + command.email()
            );
        }

        Instant now = Instant.now();
        Employee employee = Employee.builder()
            .id(UUID.randomUUID())
            .fullName(command.fullName())
            .email(command.email())
            .salary(command.salary())
            .hireDate(command.hireDate())
            .status(EmployeeStatus.ACTIVE)
            .createdAt(now)
            .updatedAt(now)
            .version(0)
            .build();

        Employee savedEmployee;
        try {
            savedEmployee = employeeRepository.save(employee);
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicateResourceException(
                "employee already exists with email: " + employee.getEmail()
            );
        }
        employeeEventPublisher.publishEmployeeCreated(savedEmployee);
        return EmployeeResult.from(savedEmployee);
    }

    @Override
    public EmployeeResult activate(UUID employeeId) {
        Employee employee = loadEmployee(employeeId);
        if (employee.getStatus() == EmployeeStatus.ACTIVE) {
            return EmployeeResult.from(employee);
        }

        Employee savedEmployee = employeeRepository.save(employee.activate());
        employeeEventPublisher.publishEmployeeActivated(savedEmployee);
        return EmployeeResult.from(savedEmployee);
    }

    @Override
    public EmployeeResult deactivate(UUID employeeId) {
        Employee employee = loadEmployee(employeeId);
        if (employee.getStatus() == EmployeeStatus.INACTIVE) {
            return EmployeeResult.from(employee);
        }

        Employee savedEmployee = employeeRepository.save(employee.deactivate());
        employeeEventPublisher.publishEmployeeDeactivated(savedEmployee);
        return EmployeeResult.from(savedEmployee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResult getById(UUID employeeId) {
        return EmployeeResult.from(loadEmployee(employeeId));
    }

    private Employee loadEmployee(UUID employeeId) {
        return employeeRepository
            .findById(employeeId)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "employee not found: " + employeeId
                )
            );
    }
}
