package org.example.banq;

import static org.example.banq.AccountNumbers.INVALID_ACCOUNT_NUMBER_LAST_DIGIT;
import static org.example.banq.AccountNumbers.VALID_ACCOUNT_NUMBER;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class AccountApiTest {

	private static final String NAME = "Test User";

	@Autowired
	private MockMvc mockMvc;

	@Test
	void createAccountThenListItAndRejectDuplicate() throws Exception {
		MvcResult created = mockMvc.perform(post("/api/v1/accounts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(accountJson()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.name").value(NAME))
				.andExpect(jsonPath("$.accountNumber").value(VALID_ACCOUNT_NUMBER))
				.andReturn();

		Number id = JsonPath.read(created.getResponse().getContentAsString(), "$.id");

		mockMvc.perform(get("/api/v1/accounts"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[?(@.id == %s)].name".formatted(id), hasItem(NAME)))
				.andExpect(jsonPath("$[?(@.id == %s)].accountNumber".formatted(id), hasItem(VALID_ACCOUNT_NUMBER)));

		mockMvc.perform(post("/api/v1/accounts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(accountJson()))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.detail").value("Account number already exists"));
	}

	@Test
	void createAccountRejectsInvalidCheckDigit() throws Exception {
		mockMvc.perform(post("/api/v1/accounts")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":"%s","accountNumber":"%s"}
								""".formatted(NAME, INVALID_ACCOUNT_NUMBER_LAST_DIGIT)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.detail").value("Account number check digit is invalid"));
	}

	@Test
	void createAccountRejectsMissingAccountNumber() throws Exception {
		mockMvc.perform(post("/api/v1/accounts")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":"%s"}
								""".formatted(NAME)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.detail").value("Account number must match NL25BANQ followed by 10 digits"));
	}

	@Test
	void createAccountRejectsNullAccountNumber() throws Exception {
		mockMvc.perform(post("/api/v1/accounts")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":"%s","accountNumber":null}
								""".formatted(NAME)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.detail").value("Account number must match NL25BANQ followed by 10 digits"));
	}

	private static String accountJson() {
		return """
				{"name":"%s","accountNumber":"%s"}
				""".formatted(NAME, VALID_ACCOUNT_NUMBER);
	}

}
