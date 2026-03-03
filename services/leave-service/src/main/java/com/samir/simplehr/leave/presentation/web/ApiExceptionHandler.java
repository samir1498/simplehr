package com.samir.simplehr.leave.presentation.web;

import com.samir.simplehr.leave.application.exception.BusinessRuleViolationException;
import com.samir.simplehr.leave.application.exception.ResourceNotFoundException;
import com.samir.simplehr.leave.application.exception.UnauthorizedOperationException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(exception.getMessage()));
	}

	@ExceptionHandler(BusinessRuleViolationException.class)
	public ResponseEntity<ApiError> handleBusinessRule(BusinessRuleViolationException exception) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(exception.getMessage()));
	}

	@ExceptionHandler(UnauthorizedOperationException.class)
	public ResponseEntity<ApiError> handleUnauthorized(UnauthorizedOperationException exception) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiError(exception.getMessage()));
	}

	@ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, IllegalArgumentException.class, IllegalStateException.class})
	public ResponseEntity<ApiError> handleBadRequest(Exception exception) {
		if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
			String message = methodArgumentNotValidException.getBindingResult()
					.getFieldErrors()
					.stream()
					.findFirst()
					.map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
					.orElse("invalid request");
			return ResponseEntity.badRequest().body(new ApiError(message));
		}
		return ResponseEntity.badRequest().body(new ApiError(exception.getMessage()));
	}

	private record ApiError(String message) {
	}
}
