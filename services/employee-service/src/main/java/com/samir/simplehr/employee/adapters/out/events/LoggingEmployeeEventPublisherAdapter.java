package com.samir.simplehr.employee.adapters.out.events;

import com.samir.simplehr.employee.application.port.out.EmployeeEventPublisherPort;
import com.samir.simplehr.employee.domain.model.Employee;
import java.time.Instant;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEmployeeEventPublisherAdapter
    implements EmployeeEventPublisherPort
{

    @Override
    public void publishEmployeeCreated(Employee employee) {
        publish("EmployeeCreated", employee);
    }

    @Override
    public void publishEmployeeActivated(Employee employee) {
        publish("EmployeeActivated", employee);
    }

    @Override
    public void publishEmployeeDeactivated(Employee employee) {
        publish("EmployeeDeactivated", employee);
    }

    private void publish(String eventType, Employee employee) {
        UUID eventId = UUID.randomUUID();
        log.info(
            "employee-event eventId={} eventType={} occurredAt={} employeeId={}",
            eventId,
            eventType,
            Instant.now(),
            employee.getId()
        );
    }
}
