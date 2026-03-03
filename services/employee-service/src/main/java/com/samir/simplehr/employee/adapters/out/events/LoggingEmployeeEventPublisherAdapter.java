package com.samir.simplehr.employee.adapters.out.events;

import com.samir.simplehr.employee.application.port.out.EmployeeEventPublisherPort;
import com.samir.simplehr.employee.domain.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class LoggingEmployeeEventPublisherAdapter implements EmployeeEventPublisherPort {

	private static final Logger log = LoggerFactory.getLogger(LoggingEmployeeEventPublisherAdapter.class);

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
		log.info("employee-event eventId={} eventType={} occurredAt={} employeeId={}", eventId, eventType, Instant.now(), employee.id());
	}
}
