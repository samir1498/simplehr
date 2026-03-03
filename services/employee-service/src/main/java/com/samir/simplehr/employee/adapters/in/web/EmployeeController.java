package com.samir.simplehr.employee.adapters.in.web;

import com.samir.simplehr.employee.adapters.in.web.dto.CreateEmployeeRequest;
import com.samir.simplehr.employee.adapters.in.web.dto.EmployeeResponse;
import com.samir.simplehr.employee.application.command.CreateEmployeeCommand;
import com.samir.simplehr.employee.application.model.EmployeeResult;
import com.samir.simplehr.employee.application.port.in.ActivateEmployeeUseCase;
import com.samir.simplehr.employee.application.port.in.CreateEmployeeUseCase;
import com.samir.simplehr.employee.application.port.in.DeactivateEmployeeUseCase;
import com.samir.simplehr.employee.application.port.in.GetEmployeeUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

	private final CreateEmployeeUseCase createEmployeeUseCase;
	private final ActivateEmployeeUseCase activateEmployeeUseCase;
	private final DeactivateEmployeeUseCase deactivateEmployeeUseCase;
	private final GetEmployeeUseCase getEmployeeUseCase;

	public EmployeeController(
			CreateEmployeeUseCase createEmployeeUseCase,
			ActivateEmployeeUseCase activateEmployeeUseCase,
			DeactivateEmployeeUseCase deactivateEmployeeUseCase,
			GetEmployeeUseCase getEmployeeUseCase
	) {
		this.createEmployeeUseCase = createEmployeeUseCase;
		this.activateEmployeeUseCase = activateEmployeeUseCase;
		this.deactivateEmployeeUseCase = deactivateEmployeeUseCase;
		this.getEmployeeUseCase = getEmployeeUseCase;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EmployeeResponse create(@Valid @RequestBody CreateEmployeeRequest request) {
		EmployeeResult employee = createEmployeeUseCase.create(
				new CreateEmployeeCommand(request.fullName(), request.email(), request.salary(), request.hireDate())
		);
		return EmployeeResponse.from(employee);
	}

	@PatchMapping("/{employeeId}/activate")
	public EmployeeResponse activate(@PathVariable UUID employeeId) {
		return EmployeeResponse.from(activateEmployeeUseCase.activate(employeeId));
	}

	@PatchMapping("/{employeeId}/deactivate")
	public EmployeeResponse deactivate(@PathVariable UUID employeeId) {
		return EmployeeResponse.from(deactivateEmployeeUseCase.deactivate(employeeId));
	}

	@GetMapping("/{employeeId}")
	public EmployeeResponse getById(@PathVariable UUID employeeId) {
		return EmployeeResponse.from(getEmployeeUseCase.getById(employeeId));
	}
}
