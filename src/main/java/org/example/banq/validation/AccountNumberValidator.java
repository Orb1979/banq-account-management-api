package org.example.banq.validation;

import java.util.regex.Pattern;

import lombok.NoArgsConstructor;
import org.example.banq.exception.InvalidAccountNumberException;

@NoArgsConstructor
public final class AccountNumberValidator {

	private static final Pattern ACCOUNT_NUMBER = Pattern.compile("NL25BANQ\\d{10}");
	private static final String FORMAT_MESSAGE = "Account number must match NL25BANQ followed by 10 digits";
	private static final String CHECK_DIGIT_MESSAGE = "Account number check digit is invalid";
	private static final int DIGITS_START = 8;
	private static final int DIGITS_END = 17;
	private static final int CHECK_DIGIT_INDEX = 17;

	public static void validate(String accountNumber) {
		if (accountNumber == null || !ACCOUNT_NUMBER.matcher(accountNumber).matches()) {
			throw new InvalidAccountNumberException(FORMAT_MESSAGE);
		}

		// The last digit must equal the sum of the first 9 digits modulo 10.
		// Example: NL25BANQ0123456786 -> 0+1+2+3+4+5+6+7+8 = 36 -> 36 % 10 = 6.
		int sum = 0;
		for (int i = DIGITS_START; i < DIGITS_END; i++) {
			sum += Character.digit(accountNumber.charAt(i), 10);
		}

		int checkDigit = Character.digit(accountNumber.charAt(CHECK_DIGIT_INDEX), 10);
		if (checkDigit != sum % 10) {
			throw new InvalidAccountNumberException(CHECK_DIGIT_MESSAGE);
		}
	}
}
