package org.example.banq.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.example.banq.dto.AccountRequestDto;
import org.example.banq.dto.AccountResponseDto;
import org.example.banq.entity.Account;
import org.example.banq.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;

	public List<AccountResponseDto> getAccounts() {
		return accountRepository.findAll().stream()
				.map(this::toResponse)
				.toList();
	}

	public AccountResponseDto createAccount(AccountRequestDto request) {
		Account account = new Account();
		account.setName(request.name());
		account.setAccountNumber(request.accountNumber());
		return toResponse(accountRepository.save(account));
	}

	private AccountResponseDto toResponse(Account account) {
		return new AccountResponseDto(account.getId(), account.getName(), account.getAccountNumber());
	}

}
