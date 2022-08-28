package com.example.tradingservice.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StockTradeRequest {
	private String userId;
	private String stockSymbol;
	private int quantity;
	private TradeType tradeType;
}
