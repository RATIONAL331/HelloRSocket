package com.example.tradingservice.dto.user;

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
