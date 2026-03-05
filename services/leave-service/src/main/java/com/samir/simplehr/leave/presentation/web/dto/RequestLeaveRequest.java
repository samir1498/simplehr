package com.samir.simplehr.leave.presentation.web.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record RequestLeaveRequest(
    @NotNull(message = "employeeId is required") UUID employeeId,
    @NotNull(message = "startDate is required") LocalDate startDate,
    @NotNull(message = "endDate is required") LocalDate endDate
) {
    @AssertTrue(message = "endDate cannot be before startDate")
    boolean isDateRangeValid() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return !endDate.isBefore(startDate);
    }

    @AssertTrue(message = "leave request must be within the same calendar year")
    boolean isSameCalendarYear() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return startDate.getYear() == endDate.getYear();
    }
}
