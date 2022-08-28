package com.example.tradingservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StockTradeResponse {
	private String userId;
	private String stockSymbol;
	private int quantity;
	private TradeType tradeType;
	private TradeStatus tradeStatus;
	private int price;
}
