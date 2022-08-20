package com.example.rsocketspring.dto.error;

import lombok.Getter;

@Getter
public enum StatusCode {
	EC001("given number is not with in range"),
	EC002("our usage limit exceeded"),
	;

	private final String description;

	StatusCode(String description) {
		this.description = description;
	}
}
