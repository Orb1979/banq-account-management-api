package org.example.banq.web;

import org.example.banq.dto.AccountRequestDto;
import org.example.banq.dto.AccountResponseDto;
import org.example.banq.exception.DuplicateAccountNumberException;
import org.example.banq.exception.InvalidAccountNumberException;
import org.example.banq.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.example.banq.AccountNumbers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@Import(ApiExceptionHandler.class)
class AccountControllerTest {

	private static final String NAME = "test name";

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AccountService accountService;

	@Test
	void createAccount() throws Exception {
		AccountResponseDto response = new AccountResponseDto(1L, NAME, VALID_ACCOUNT_NUMBER);
		when(accountService.createAccount(any(AccountRequestDto.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/accounts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(accountJson(VALID_ACCOUNT_NUMBER)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value(NAME))
				.andExpect(jsonPath("$.accountNumber").value(VALID_ACCOUNT_NUMBER))
				.andExpect(jsonPath("$.id").isNumber());
	}

	@Test
	void listAccounts() throws Exception {
		AccountResponseDto response = new AccountResponseDto(1L, NAME, VALID_ACCOUNT_NUMBER);
		when(accountService.getAccounts()).thenReturn(List.of(response));

		mockMvc.perform(get("/api/v1/accounts"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value(NAME))
				.andExpect(jsonPath("$[0].accountNumber").value(VALID_ACCOUNT_NUMBER));
	}

	@Test
	void createAccountRejectsBlankName() throws Exception {
		mockMvc.perform(post("/api/v1/accounts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(accountJson("   ", VALID_ACCOUNT_NUMBER)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.detail").value("Name is required"));
	}

	@Test
	void createAccountRejectsTooLongName() throws Exception {
		mockMvc.perform(post("/api/v1/accounts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(accountJson("a".repeat(51), VALID_ACCOUNT_NUMBER)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.detail").value("Name must be at most 50 characters"));
	}

	private static String accountJson(String accountNumber) {
		return accountJson(NAME, accountNumber);
	}

	private static String accountJson(String name, String accountNumber) {
		return """
				{"name":"%s","accountNumber":"%s"}
				""".formatted(name, accountNumber);
	}

}
