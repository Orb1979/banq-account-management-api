package org.example.banq.exception;

public class DuplicateAccountNumberException extends RuntimeException {

	public DuplicateAccountNumberException(String message) {
		super(message);
	}

	public DuplicateAccountNumberException(String message, Throwable cause) {
		super(message, cause);
	}

}
