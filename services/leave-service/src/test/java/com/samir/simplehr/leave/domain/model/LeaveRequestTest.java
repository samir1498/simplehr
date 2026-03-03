package com.samir.simplehr.leave.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LeaveRequestTest {

	@Test
	void shouldComputeRequestedDaysInclusively() {
		LeaveRequest leaveRequest = LeaveRequest.request(
				UUID.randomUUID(),
				LocalDate.of(2026, 9, 1),
				LocalDate.of(2026, 9, 3)
		);

		assertEquals(3, leaveRequest.daysRequested());
	}

	@Test
	void shouldRejectCrossYearLeaveRequest() {
		assertThrows(IllegalArgumentException.class, () -> LeaveRequest.request(
				UUID.randomUUID(),
				LocalDate.of(2026, 12, 31),
				LocalDate.of(2027, 1, 2)
		));
	}
}
