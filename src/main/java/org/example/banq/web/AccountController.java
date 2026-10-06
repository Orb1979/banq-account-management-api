package org.example.banq.web;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.example.banq.dto.AccountRequestDto;
import org.example.banq.dto.AccountResponseDto;
import org.example.banq.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<AccountResponseDto> getAccounts() {
		return accountService.getAccounts();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public AccountResponseDto createAccount(@RequestBody AccountRequestDto request) {
		return accountService.createAccount(request);
	}

}
