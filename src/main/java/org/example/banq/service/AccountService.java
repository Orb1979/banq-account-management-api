package org.example.banq.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.example.banq.dto.AccountRequestDto;
import org.example.banq.dto.AccountResponseDto;
import org.example.banq.entity.Account;
import org.example.banq.exception.DuplicateAccountNumberException;
import org.example.banq.repository.AccountRepository;
import org.example.banq.validation.AccountNumberValidator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

	private static final String DUPLICATE_MESSAGE = "Account number already exists";

	private final AccountRepository accountRepository;

	public List<AccountResponseDto> getAccounts() {
		return accountRepository.findAll().stream()
				.map(this::toResponse)
				.toList();
	}

	public AccountResponseDto createAccount(AccountRequestDto request) {
		AccountNumberValidator.validate(request.accountNumber());
		if (accountRepository.existsByAccountNumber(request.accountNumber())) {
			throw new DuplicateAccountNumberException(DUPLICATE_MESSAGE);
		}

		Account account = new Account();
		account.setName(request.name());
		account.setAccountNumber(request.accountNumber());
		try {
			return toResponse(accountRepository.save(account));
		} catch (DataIntegrityViolationException exception) {
			throw new DuplicateAccountNumberException(DUPLICATE_MESSAGE, exception);
		}
	}

	private AccountResponseDto toResponse(Account account) {
		return new AccountResponseDto(account.getId(), account.getName(), account.getAccountNumber());
	}
}
