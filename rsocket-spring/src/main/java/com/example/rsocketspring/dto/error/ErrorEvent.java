package com.example.rsocketspring.dto.error;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ErrorEvent {
	private StatusCode statusCode;
	private final LocalDate date = LocalDate.now();
}
