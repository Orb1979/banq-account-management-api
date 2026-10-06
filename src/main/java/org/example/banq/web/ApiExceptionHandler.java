package org.example.banq.web;

import org.example.banq.exception.DuplicateAccountNumberException;
import org.example.banq.exception.InvalidAccountNumberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(InvalidAccountNumberException.class)
	public ProblemDetail handleInvalidAccountNumber(InvalidAccountNumberException exception) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
	}

	@ExceptionHandler(DuplicateAccountNumberException.class)
	public ProblemDetail handleDuplicateAccountNumber(DuplicateAccountNumberException exception) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
	}
}
