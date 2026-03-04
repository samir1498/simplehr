package com.samir.simplehr.payroll.adapters.in.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samir.simplehr.payroll.application.model.EmployeeCreatedEvent;
import com.samir.simplehr.payroll.application.model.LeaveApprovedEvent;
import com.samir.simplehr.payroll.application.port.in.HandleEmployeeCreatedUseCase;
import com.samir.simplehr.payroll.application.port.in.HandleLeaveApprovedUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class PayrollKafkaConsumer {

	private static final Logger log = LoggerFactory.getLogger(PayrollKafkaConsumer.class);

	private final ObjectMapper objectMapper;
	private final HandleEmployeeCreatedUseCase handleEmployeeCreatedUseCase;
	private final HandleLeaveApprovedUseCase handleLeaveApprovedUseCase;

	public PayrollKafkaConsumer(
			ObjectMapper objectMapper,
			HandleEmployeeCreatedUseCase handleEmployeeCreatedUseCase,
			HandleLeaveApprovedUseCase handleLeaveApprovedUseCase
	) {
		this.objectMapper = objectMapper;
		this.handleEmployeeCreatedUseCase = handleEmployeeCreatedUseCase;
		this.handleLeaveApprovedUseCase = handleLeaveApprovedUseCase;
	}

	@KafkaListener(topics = {"employee.events", "leave.events"}, groupId = "${app.kafka.group-id:payroll-service}")
	public void consume(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		try {
			JsonNode root = objectMapper.readTree(message);
			UUID eventId = UUID.fromString(root.path("eventId").asText());
			String eventType = root.path("eventType").asText();
			JsonNode payload = root.path("payload");

			switch (eventType) {
				case "EmployeeCreated" -> handleEmployeeCreatedUseCase.handle(new EmployeeCreatedEvent(
						eventId,
						UUID.fromString(payload.path("employeeId").asText()),
						new BigDecimal(payload.path("salary").asText())
				));
				case "LeaveApproved" -> handleLeaveApprovedUseCase.handle(new LeaveApprovedEvent(
						eventId,
						UUID.fromString(payload.path("employeeId").asText()),
						UUID.fromString(payload.path("leaveRequestId").asText()),
						LocalDate.parse(payload.path("startDate").asText()),
						LocalDate.parse(payload.path("endDate").asText())
				));
				default -> log.debug("ignoring unsupported eventType={} from topic={}", eventType, topic);
			}
		} catch (IOException | RuntimeException exception) {
			log.error("failed to process message from topic={} payload={}", topic, message, exception);
		}
	}
}
