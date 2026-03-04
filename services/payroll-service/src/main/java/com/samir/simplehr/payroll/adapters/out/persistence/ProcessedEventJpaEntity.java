package com.samir.simplehr.payroll.adapters.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "processed_events")
public class ProcessedEventJpaEntity {
	@Id
	@Column(name = "event_id", nullable = false, updatable = false)
	private UUID eventId;

	@Column(name = "event_type", nullable = false, length = 100)
	private String eventType;

	@Column(name = "processed_at", nullable = false)
	private Instant processedAt;

	public UUID getEventId() { return eventId; }
	public void setEventId(UUID eventId) { this.eventId = eventId; }
	public String getEventType() { return eventType; }
	public void setEventType(String eventType) { this.eventType = eventType; }
	public Instant getProcessedAt() { return processedAt; }
	public void setProcessedAt(Instant processedAt) { this.processedAt = processedAt; }
}
