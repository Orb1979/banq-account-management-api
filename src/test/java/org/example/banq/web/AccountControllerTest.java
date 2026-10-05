package org.example.banq.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void createAccountThenListIt() throws Exception {
		mockMvc.perform(post("/api/v1/accounts")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":"test name","accountNumber":"NL25BANQ0123456789"}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("test name"))
				.andExpect(jsonPath("$.accountNumber").value("NL25BANQ0123456789"))
				.andExpect(jsonPath("$.id").isNumber());

		mockMvc.perform(get("/api/v1/accounts"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("test name"))
				.andExpect(jsonPath("$[0].accountNumber").value("NL25BANQ0123456789"));
	}
}
