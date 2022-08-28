package com.example.tradingservice.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransactionResponse {
	private String userId;
	private int amount;
	private TransactionType type;
	private TransactionStatus status;
}
