package org.example.banq.validation;

import static org.example.banq.AccountNumbers.INVALID_ACCOUNT_NUMBER;
import static org.example.banq.AccountNumbers.INVALID_ACCOUNT_NUMBER_LAST_DIGIT;
import static org.example.banq.AccountNumbers.VALID_ACCOUNT_NUMBER;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.example.banq.exception.InvalidAccountNumberException;
import org.junit.jupiter.api.Test;

class AccountNumberValidatorTest {

	@Test
	void acceptsAccountNumberWithValidCheckDigit() {
		assertDoesNotThrow(() -> AccountNumberValidator.validate(VALID_ACCOUNT_NUMBER));
	}

	@Test
	void rejectsAccountNumberWithInvalidCheckDigit() {
		InvalidAccountNumberException exception = assertThrows(InvalidAccountNumberException.class,
				() -> AccountNumberValidator.validate(INVALID_ACCOUNT_NUMBER_LAST_DIGIT));

		assertEquals("Account number check digit is invalid", exception.getMessage());
	}

	@Test
	void rejectsAccountNumberWithWrongFormat() {
		InvalidAccountNumberException exception = assertThrows(InvalidAccountNumberException.class,
				() -> AccountNumberValidator.validate(INVALID_ACCOUNT_NUMBER));

		assertEquals("Account number must match NL25BANQ followed by 10 digits", exception.getMessage());
	}
}
