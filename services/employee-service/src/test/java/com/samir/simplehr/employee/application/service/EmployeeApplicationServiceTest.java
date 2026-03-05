package com.samir.simplehr.employee.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.samir.simplehr.employee.application.command.CreateEmployeeCommand;
import com.samir.simplehr.employee.application.exception.DuplicateResourceException;
import com.samir.simplehr.employee.application.model.EmployeeResult;
import com.samir.simplehr.employee.application.port.out.EmployeeEventPublisherPort;
import com.samir.simplehr.employee.application.port.out.EmployeeRepositoryPort;
import com.samir.simplehr.employee.domain.model.Employee;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

class EmployeeApplicationServiceTest {

    @Test
    void shouldCreateEmployeeWithStubbedDependencies() {
        InMemoryEmployeeRepository repository =
            new InMemoryEmployeeRepository();
        RecordingEmployeeEventPublisher publisher =
            new RecordingEmployeeEventPublisher();
        EmployeeApplicationService service = new EmployeeApplicationService(
            repository,
            publisher
        );

        CreateEmployeeCommand command = new CreateEmployeeCommand(
            "Samir B",
            " Samir@Test.com ",
            new BigDecimal("150000"),
            LocalDate.of(2026, 3, 3)
        );

        EmployeeResult result = service.create(command);

        assertNotNull(result.id());
        assertEquals("samir@test.com", result.email());
        assertEquals("samir@test.com", publisher.createdEmail);
        assertEquals(1, repository.savedEmployeesById.size());
    }

    @Test
    void shouldThrowDuplicateWhenEmailAlreadyExistsBeforeSave() {
        InMemoryEmployeeRepository repository =
            new InMemoryEmployeeRepository();
        repository.existingEmailCheckResult = true;
        RecordingEmployeeEventPublisher publisher =
            new RecordingEmployeeEventPublisher();
        EmployeeApplicationService service = new EmployeeApplicationService(
            repository,
            publisher
        );

        CreateEmployeeCommand command = new CreateEmployeeCommand(
            "Samir B",
            "samir@test.com",
            new BigDecimal("150000"),
            LocalDate.of(2026, 3, 3)
        );

        DuplicateResourceException exception = assertThrows(
            DuplicateResourceException.class,
            () -> service.create(command)
        );

        assertEquals(
            "employee already exists with email: samir@test.com",
            exception.getMessage()
        );
        assertNull(publisher.createdEmail);
        assertEquals(0, repository.savedEmployeesById.size());
    }

    @Test
    void shouldThrowDuplicateWhenSaveHitsUniqueConstraint() {
        InMemoryEmployeeRepository repository =
            new InMemoryEmployeeRepository();
        repository.throwOnSave = true;
        RecordingEmployeeEventPublisher publisher =
            new RecordingEmployeeEventPublisher();
        EmployeeApplicationService service = new EmployeeApplicationService(
            repository,
            publisher
        );

        CreateEmployeeCommand command = new CreateEmployeeCommand(
            "Samir B",
            "samir@test.com",
            new BigDecimal("150000"),
            LocalDate.of(2026, 3, 3)
        );

        DuplicateResourceException exception = assertThrows(
            DuplicateResourceException.class,
            () -> service.create(command)
        );

        assertEquals(
            "employee already exists with email: samir@test.com",
            exception.getMessage()
        );
        assertNull(publisher.createdEmail);
    }

    private static final class InMemoryEmployeeRepository
        implements EmployeeRepositoryPort
    {

        private final Map<UUID, Employee> savedEmployeesById = new HashMap<>();
        private boolean existingEmailCheckResult;
        private boolean throwOnSave;

        @Override
        public Optional<Employee> findById(UUID employeeId) {
            return Optional.ofNullable(savedEmployeesById.get(employeeId));
        }

        @Override
        public boolean existsByEmail(String email) {
            return existingEmailCheckResult;
        }

        @Override
        public Employee save(Employee employee) {
            if (throwOnSave) {
                throw new DataIntegrityViolationException("duplicate email");
            }
            savedEmployeesById.put(employee.getId(), employee);
            return employee;
        }
    }

    private static final class RecordingEmployeeEventPublisher
        implements EmployeeEventPublisherPort
    {

        private String createdEmail;

        @Override
        public void publishEmployeeCreated(Employee employee) {
            createdEmail = employee.getEmail();
        }

        @Override
        public void publishEmployeeActivated(Employee employee) {}

        @Override
        public void publishEmployeeDeactivated(Employee employee) {}
    }
}
