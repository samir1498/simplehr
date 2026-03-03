package com.samir.simplehr.leave.application.exception;

public class BusinessRuleViolationException extends RuntimeException {
	public BusinessRuleViolationException(String message) {
		super(message);
	}
}
