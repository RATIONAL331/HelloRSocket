package com.example.userservice.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
	private String userId;
	private int amount;
	private TransactionType type;
}
