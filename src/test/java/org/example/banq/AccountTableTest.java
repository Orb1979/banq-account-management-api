package org.example.banq;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class AccountTableTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void accountTableIsCreated() {
		Integer tables = jdbcTemplate.queryForObject("""
				SELECT COUNT(*)
				FROM INFORMATION_SCHEMA.TABLES
				WHERE TABLE_SCHEMA = 'PUBLIC'
				  AND TABLE_NAME = 'ACCOUNT'
				""", Integer.class);

		assertEquals(1, tables);
	}

}
