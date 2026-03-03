package com.samir.simplehr.employee.application.exception;

public class DuplicateResourceException extends RuntimeException {
	public DuplicateResourceException(String message) {
		super(message);
	}
}
