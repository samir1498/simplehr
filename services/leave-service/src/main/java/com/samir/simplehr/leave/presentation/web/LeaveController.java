package com.samir.simplehr.leave.presentation.web;

import com.samir.simplehr.leave.application.command.RequestLeaveCommand;
import com.samir.simplehr.leave.application.command.ReviewLeaveCommand;
import com.samir.simplehr.leave.application.port.in.ApproveLeaveUseCase;
import com.samir.simplehr.leave.application.port.in.GetLeaveRequestUseCase;
import com.samir.simplehr.leave.application.port.in.RejectLeaveUseCase;
import com.samir.simplehr.leave.application.port.in.RequestLeaveUseCase;
import com.samir.simplehr.leave.presentation.web.dto.LeaveRequestResponse;
import com.samir.simplehr.leave.presentation.web.dto.RequestLeaveRequest;
import com.samir.simplehr.leave.presentation.web.dto.ReviewLeaveRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/leaves")
public class LeaveController {

	private final RequestLeaveUseCase requestLeaveUseCase;
	private final ApproveLeaveUseCase approveLeaveUseCase;
	private final RejectLeaveUseCase rejectLeaveUseCase;
	private final GetLeaveRequestUseCase getLeaveRequestUseCase;

	public LeaveController(
			RequestLeaveUseCase requestLeaveUseCase,
			ApproveLeaveUseCase approveLeaveUseCase,
			RejectLeaveUseCase rejectLeaveUseCase,
			GetLeaveRequestUseCase getLeaveRequestUseCase
	) {
		this.requestLeaveUseCase = requestLeaveUseCase;
		this.approveLeaveUseCase = approveLeaveUseCase;
		this.rejectLeaveUseCase = rejectLeaveUseCase;
		this.getLeaveRequestUseCase = getLeaveRequestUseCase;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public LeaveRequestResponse requestLeave(@Valid @RequestBody RequestLeaveRequest request) {
		return LeaveRequestResponse.from(requestLeaveUseCase.requestLeave(
				new RequestLeaveCommand(request.employeeId(), request.startDate(), request.endDate())
		));
	}

	@PatchMapping("/{leaveRequestId}/approve")
	public LeaveRequestResponse approveLeave(
			@PathVariable UUID leaveRequestId,
			@RequestHeader(name = "X-Role", required = false) String role,
			@RequestBody(required = false) ReviewLeaveRequest request
	) {
		ReviewLeaveRequest safeRequest = request == null ? new ReviewLeaveRequest(null, null) : request;
		return LeaveRequestResponse.from(approveLeaveUseCase.approveLeave(
				leaveRequestId,
				new ReviewLeaveCommand(role, safeRequest.reviewerId(), null)
		));
	}

	@PatchMapping("/{leaveRequestId}/reject")
	public LeaveRequestResponse rejectLeave(
			@PathVariable UUID leaveRequestId,
			@RequestHeader(name = "X-Role", required = false) String role,
			@RequestBody(required = false) ReviewLeaveRequest request
	) {
		ReviewLeaveRequest safeRequest = request == null ? new ReviewLeaveRequest(null, null) : request;
		return LeaveRequestResponse.from(rejectLeaveUseCase.rejectLeave(
				leaveRequestId,
				new ReviewLeaveCommand(role, safeRequest.reviewerId(), safeRequest.rejectionReason())
		));
	}

	@GetMapping("/{leaveRequestId}")
	public LeaveRequestResponse getById(@PathVariable UUID leaveRequestId) {
		return LeaveRequestResponse.from(getLeaveRequestUseCase.getById(leaveRequestId));
	}
}
