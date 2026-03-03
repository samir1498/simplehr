package com.samir.simplehr.payroll.domain.model;

import java.time.Instant;
import java.util.UUID;

public record ProcessedEvent(
		UUID eventId,
		String eventType,
		Instant processedAt
) {
	public ProcessedEvent {
		if (eventId == null) throw new IllegalArgumentException("eventId is required");
		if (eventType == null || eventType.isBlank()) throw new IllegalArgumentException("eventType is required");
		if (processedAt == null) throw new IllegalArgumentException("processedAt is required");
	}

	public static ProcessedEvent from(UUID eventId, String eventType) {
		return new ProcessedEvent(eventId, eventType, Instant.now());
	}
}
