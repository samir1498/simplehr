package com.samir.simplehr.payroll.application.port.out;

import com.samir.simplehr.payroll.domain.model.ProcessedEvent;

import java.util.UUID;

public interface ProcessedEventRepositoryPort {
	boolean existsByEventId(UUID eventId);

	void save(ProcessedEvent processedEvent);
}
