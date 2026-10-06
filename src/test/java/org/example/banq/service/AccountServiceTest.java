package org.example.banq.service;

import static org.example.banq.AccountNumbers.INVALID_ACCOUNT_NUMBER;
import static org.example.banq.AccountNumbers.INVALID_ACCOUNT_NUMBER_LAST_DIGIT;
import static org.example.banq.AccountNumbers.VALID_ACCOUNT_NUMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.dao.DataIntegrityViolationException;

import org.example.banq.dto.AccountRequestDto;
import org.example.banq.dto.AccountResponseDto;
import org.example.banq.entity.Account;
import org.example.banq.exception.DuplicateAccountNumberException;
import org.example.banq.exception.InvalidAccountNumberException;
import org.example.banq.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

	@Mock private AccountRepository accountRepository;
	@InjectMocks private AccountService accountService;

	@Test
	void acceptsAccountNumberWithValidCheckDigit() {
		// Arrange
		AccountRepository accountRepository = mock(AccountRepository.class);
		when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
			Account account = invocation.getArgument(0);
			account.setId(1L);
			return account;
		});

		// Act
		AccountService service = new AccountService(accountRepository);
		AccountResponseDto response = service.createAccount(new AccountRequestDto("test name", VALID_ACCOUNT_NUMBER));

		// Verify
		assertEquals(VALID_ACCOUNT_NUMBER, response.accountNumber());
	}

	@Test
	void rejectsAccountNumberWithInvalidCheckDigit() {
		// Arrange + Act
		InvalidAccountNumberException exception = assertThrows(InvalidAccountNumberException.class,
				() -> accountService.createAccount(new AccountRequestDto("test name", INVALID_ACCOUNT_NUMBER_LAST_DIGIT)));

		// Verify
		assertEquals("Account number check digit is invalid", exception.getMessage());
	}

	@Test
	void rejectsDuplicateAccountNumber() {
		// Arrange
		when(accountRepository.existsByAccountNumber(VALID_ACCOUNT_NUMBER)).thenReturn(true);

		// Act
		DuplicateAccountNumberException exception = assertThrows(DuplicateAccountNumberException.class,
				() -> accountService.createAccount(new AccountRequestDto("test name", VALID_ACCOUNT_NUMBER)));

		// Verify
		assertEquals("Account number already exists", exception.getMessage());
	}

	@Test
	void mapsUniqueConstraintViolationToDuplicateAccount() {
		// Arrange: existsByAccountNumber
		DataIntegrityViolationException constraintViolation =
				new DataIntegrityViolationException("uk_account_account_number");
 		when(accountRepository.save(any(Account.class))).thenThrow(constraintViolation);

		// Act
		DuplicateAccountNumberException exception = assertThrows(DuplicateAccountNumberException.class,
				() -> accountService.createAccount(new AccountRequestDto("test name", VALID_ACCOUNT_NUMBER)));

		// Verify
		assertEquals("Account number already exists", exception.getMessage());
		assertEquals(constraintViolation, exception.getCause());
	}

	@Test
	void rejectsAccountNumberWithWrongFormat() {
		// Arrange + Act
		InvalidAccountNumberException exception = assertThrows(InvalidAccountNumberException.class,
				() -> accountService.createAccount(new AccountRequestDto("test name", INVALID_ACCOUNT_NUMBER)));

		// Verify
		assertEquals("Account number must match NL25BANQ followed by 10 digits", exception.getMessage());
	}
}
