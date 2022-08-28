package com.example.tradingservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserStockDto {
	private String id;
	private String userId;
	private String stockSymbol;
	private Integer quantity;
}
