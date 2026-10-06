package org.example.banq.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AccountRequestDto(
		@NotBlank(message = "Name is required")
		@Size(max = 50, message = "Name must be at most {max} characters")
		String name,
		String accountNumber) {
}
