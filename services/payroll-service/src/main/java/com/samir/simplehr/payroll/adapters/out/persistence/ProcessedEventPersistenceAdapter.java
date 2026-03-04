package com.samir.simplehr.payroll.adapters.out.persistence;

import com.samir.simplehr.payroll.application.port.out.ProcessedEventRepositoryPort;
import com.samir.simplehr.payroll.domain.model.ProcessedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProcessedEventPersistenceAdapter implements ProcessedEventRepositoryPort {

	private final SpringDataProcessedEventRepository processedEventRepository;

	public ProcessedEventPersistenceAdapter(SpringDataProcessedEventRepository processedEventRepository) {
		this.processedEventRepository = processedEventRepository;
	}

	@Override
	public boolean existsByEventId(UUID eventId) {
		return processedEventRepository.existsById(eventId);
	}

	@Override
	public void save(ProcessedEvent processedEvent) {
		ProcessedEventJpaEntity entity = new ProcessedEventJpaEntity();
		entity.setEventId(processedEvent.eventId());
		entity.setEventType(processedEvent.eventType());
		entity.setProcessedAt(processedEvent.processedAt());
		processedEventRepository.save(entity);
	}
}
