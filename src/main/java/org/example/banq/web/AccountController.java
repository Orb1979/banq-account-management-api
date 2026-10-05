package org.example.banq.web;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.example.banq.dto.AccountRequestDto;
import org.example.banq.dto.AccountResponseDto;
import org.example.banq.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;

	@GetMapping
	public List<AccountResponseDto> getAccounts() {
		return accountService.getAccounts();
	}

	@PostMapping
	public AccountResponseDto createAccount(@RequestBody AccountRequestDto request) {
		return accountService.createAccount(request);
	}

}
